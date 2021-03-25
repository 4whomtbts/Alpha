package com.dna.rna.service.util;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.gpu.Gpu;
import com.dna.rna.domain.instanceGpu.InstanceGpu;
import com.dna.rna.domain.server.Server;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.dna.rna.service.util.InstanceResourceAllocator.MAX_GPU_NUM;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InstanceResourceAllocator2Test {

    private InstanceResourceAllocator instanceResourceAllocator;

    @Before
    public void setUp() {
        instanceResourceAllocator = new InstanceResourceAllocator();
    }

    private List<Server> getFreeGPUList() {
        List<Server> serverList = new ArrayList<>();
        List<Integer> allFreeGPU = new ArrayList<>();
        for (int i=0; i < 6; i++) {
            Server server = new Server(i+1, "192.168.1.11"+i, 8081+i, 9000 + (i*100),
                    new ServerResource(new ArrayList<>(allFreeGPU)));
            List<Gpu> gpuList = new ArrayList<>();
            for (int j=0; j < MAX_GPU_NUM; j++) {
                gpuList.add(new Gpu(server, j, UUID.randomUUID().toString(), "GTX 3080"));
            }
            server.setGpuList(gpuList);
            serverList.add(server);
        }
        return serverList;
    }

    @Test
    public void allocGpu_PASS_AND_FILL_UP_SERVER1() {
        List<Server> list = getFreeGPUList();
        int requestGpuNum = MAX_GPU_NUM;
        InstanceResourceAllocator.AllocResult allocResult = instanceResourceAllocator.allocGpu(list, requestGpuNum, true);
        List<InstanceGpu> createdInstanceGpuList = allocResult.getInstanceGpuList();
        final Server first_server = list.get(0);
        assertThat(allocResult.getServer(), is(first_server));
        assertThat(createdInstanceGpuList.size(), is(requestGpuNum));
        for (int i=0; i < createdInstanceGpuList.size(); i++) {
            InstanceGpu instanceGpu = createdInstanceGpuList.get(i);
            assertTrue(instanceGpu.isExclusivelyOccupied());
            assertThat(instanceGpu.getServer(), is(first_server));
        }
    }

    @Test
    public void allocGpu_현재_서버에_GPU가_모두_점유중이면_다음의_가용_서버에_할당한다() {
        List<Server> list = getFreeGPUList();
        int requestGpuNum = MAX_GPU_NUM;
        InstanceResourceAllocator.AllocResult allocResult = instanceResourceAllocator.allocGpu(list, requestGpuNum, true);
        InstanceResourceAllocator.AllocResult allocResult1 = instanceResourceAllocator.allocGpu(list, requestGpuNum, true);
        
        List<InstanceGpu> createdInstanceGpuList = allocResult.getInstanceGpuList();
        final Server first_server = list.get(0);
        assertThat(allocResult.getServer(), is(first_server));
        assertThat(createdInstanceGpuList.size(), is(requestGpuNum));
        for (int i=0; i < createdInstanceGpuList.size(); i++) {
            InstanceGpu instanceGpu = createdInstanceGpuList.get(i);
            assertTrue(instanceGpu.isExclusivelyOccupied());
            assertThat(instanceGpu.getServer(), is(first_server));
        }
    }
}
