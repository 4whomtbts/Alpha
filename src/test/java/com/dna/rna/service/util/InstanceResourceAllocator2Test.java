package com.dna.rna.service.util;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.gpu.Gpu;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instanceGpu.InstanceGpu;
import com.dna.rna.domain.server.Server;
import com.dna.rna.exception.DCloudException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.dna.rna.service.util.InstanceResourceAllocator.MAX_GPU_NUM;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InstanceResourceAllocator2Test {

    private InstanceResourceAllocator instanceResourceAllocator;
    private static final int NUM_SERVER = 6;
    private static final int NUM_GPU_IN_SERVER = 8;

    @Before
    public void setUp() {
        instanceResourceAllocator = new InstanceResourceAllocator();
    }

    private List<Server> getFreeGPUList() {
        List<Server> serverList = new ArrayList<>();
        List<Integer> allFreeGPU = new ArrayList<>();
        for (int i=0; i < NUM_SERVER; i++) {
            Server server = new Server(i+1, "192.168.1.1"+(i+1), 8081+i, 9000 + (i*100),
                    new ServerResource(new ArrayList<>(allFreeGPU)));
            List<Gpu> gpuList = new ArrayList<>();
            for (int j=0; j < NUM_GPU_IN_SERVER; j++) {
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
        List<Gpu> selectedGpuList = allocResult.getGpuList();
        List<InstanceGpu> createdInstanceGpuList = new ArrayList<>();
        selectedGpuList.forEach(gpu -> createdInstanceGpuList.add(new InstanceGpu(new Instance(gpu.getServer()), gpu, true)));

        final Server first_server = list.get(0);
        assertThat(allocResult.getServer(), is(first_server));
        assertThat(createdInstanceGpuList.size(), is(requestGpuNum));
        for (int i=0; i < createdInstanceGpuList.size(); i++) {
            InstanceGpu instanceGpu = createdInstanceGpuList.get(i);
            assertTrue(instanceGpu.isExclusivelyOccupied());
            assertThat(instanceGpu.getInstance().getServer(), is(first_server));
        }
    }

    @Test
    public void allocGpu_현재_서버에_GPU가_모두_점유중이면_다음의_가용_서버에_할당한다() {
        List<Server> list = getFreeGPUList();
        Server firstServer = list.get(0);
        for (Gpu gpu : firstServer.getGpuList()) {
            InstanceGpu instanceGpu = new InstanceGpu(new Instance(firstServer), gpu, true);
            gpu.setInstanceGpuList(Arrays.asList(instanceGpu));
        }
        int requestGpuNum = MAX_GPU_NUM;
        InstanceResourceAllocator.AllocResult allocResult = instanceResourceAllocator.allocGpu(list, requestGpuNum, true);
        List<Gpu> selectedGpuList = allocResult.getGpuList();
        List<InstanceGpu> createdInstanceGpuList = new ArrayList<>();
        selectedGpuList.forEach(gpu -> createdInstanceGpuList.add(new InstanceGpu(new Instance(gpu.getServer()), gpu, true)));

        Server secondServer = list.get(1);
        assertThat(allocResult.getServer(), is(secondServer));
        assertThat(createdInstanceGpuList.size(), is(requestGpuNum));
        for (int i=0; i < createdInstanceGpuList.size(); i++) {
            InstanceGpu instanceGpu = createdInstanceGpuList.get(i);
            assertTrue(instanceGpu.isExclusivelyOccupied());
            assertThat(instanceGpu.getInstance().getServer(), is(list.get(1)));
        }
    }

    @Test(expected = DCloudException.class)
    public void allocGpu_모든_서버의_GPU가_독점적으로_사용되면_예외를_발생시킨다() {
        List<Server> list = getFreeGPUList();
        for (Server server : list) {
            for (Gpu gpu : server.getGpuList()) {
                InstanceGpu instanceGpu = new InstanceGpu(new Instance(server), gpu, true);
                gpu.setInstanceGpuList(Arrays.asList(instanceGpu));
            }
        }
        instanceResourceAllocator.allocGpu(list, 1, true);
    }

    /*
         비독점적으로 GPU가 할당된 경우에, 동일한 GPU가 복수번 할당될 수 있다
         이러한 경우 하나의 서버에서만 GPU가 할당될 수 있기 때문에
         서버는 자신이 최종적으로 할당받은 시간을 기억하고 있으며
         GPU는 자신에게 할당된 컨테이너의 개수를 알고있는다
         그래서, 매 할당시에 서버의 최종 할당시간 오름차순과 GPU 할당횟수 오름차순으로 정렬하여
         모든 서버와 서버내의 GPU가 고르게 할당받을 수 있도록 한다.

         이 테스트가 하고자 하는것은 각 x개의 GPU를 가지고 있는 y개의 서버에
         xy개의 GPU 할당을 요청하였을 때, 모든 GPU가 한 번 씩만 할당되는지 확인하는 것이다.
     */
    @Test
    public void allocGpu_비독점적_할당시에는_모든_서버가_고르게_할당받는다() {
        List<Server> list = getFreeGPUList();

        for (int i = 0; i < list.size(); i++) {
            InstanceResourceAllocator.AllocResult allocResult = instanceResourceAllocator.allocGpu(list, MAX_GPU_NUM, false);
            Server server = allocResult.getServer();
            for (Gpu gpu : server.getGpuList()) {
                InstanceGpu instanceGpu = new InstanceGpu(new Instance(server), gpu, true);
                gpu.setInstanceGpuList(Arrays.asList(instanceGpu));
            }
        }

        for (Server server : list) {
            for (Gpu gpu : server.getGpuList()) {
                assertThat(gpu.getInstanceGpuList().size(), is(1));
            }
        }
    }

    @Test
    public void allocGpu_모든_서버의_모든_GPU가_한번씩_비독점적으로_할당된_상태에서_새로_할당하면_가장_처음에_할당받은_서버에_할당된다() {
        List<Server> list = getFreeGPUList();

        for (int i = 0; i < list.size(); i++) {
            InstanceResourceAllocator.AllocResult allocResult = instanceResourceAllocator.allocGpu(list, MAX_GPU_NUM, false);
            Server server = allocResult.getServer();
            for (Gpu gpu : server.getGpuList()) {
                InstanceGpu instanceGpu = new InstanceGpu(new Instance(server), gpu, false);
                gpu.setInstanceGpuList(Arrays.asList(instanceGpu));
            }
        }
        InstanceResourceAllocator.AllocResult allocResult = instanceResourceAllocator.allocGpu(list, 1, false);
        Server allocatedServer = allocResult.getServer();
        assertThat(allocatedServer, is(list.get(0)));
    }

}
