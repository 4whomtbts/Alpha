package com.dna.rna.service.util;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.instance.InstanceRepository;
import com.dna.rna.domain.server.Server;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dna.rna.service.util.InstanceResourceAllocator.MAX_GPU_NUM;
import static org.assertj.core.api.Assertions.assertThat;

public class InstanceResourceAllocatorTest {

    private InstanceResourceAllocator instanceResourceAllocator;

    @Before
    public void setUp() {
        instanceResourceAllocator = new InstanceResourceAllocator();
    }

    private List<Server> getFreeGPUList() {
        List<Server> serverList = new ArrayList<>();
        List<Integer> allFreeGPU = new ArrayList<>();
        for (int i=0; i < MAX_GPU_NUM; i++) {
            allFreeGPU.add(ServerResource.UN_ALLOC);
        }
        for (int i=0; i < 6; i++) {
            serverList.add(new Server(i+1, "192.168.1.11"+i, 8081+i, 9000 + (i*100),
                    new ServerResource(new ArrayList<>(allFreeGPU))));
        }
        return serverList;
    }

    @Test
    public void allocateGPU_PASS_AND_FILL_UP_SERVER1() {
        List<Server> list = getFreeGPUList();
        int requestGpuNum = MAX_GPU_NUM;
        InstanceResourceAllocator.AllocationResult result1 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        assertThat(result1.getIndex()).isEqualTo(0);
        List<Integer> gpus = new ArrayList<>();
        for (int i=0; i < requestGpuNum; i++) {
            gpus.add(ServerResource.EXCLUSIVELY_ALLOC);
        }
        for (int i=0; i < result1.getGpus().size(); i++) {
            assertThat(result1.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }
    }

    @Test
    public void allocateGPU_PASS_2_AND_FILL_UP_SERVERS1_2() {
        List<Server> list = getFreeGPUList();
        int requestGpuNum = MAX_GPU_NUM;
        InstanceResourceAllocator.AllocationResult result1 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result1.getIndex()).getServerResource().setGpus(result1.getGpus());
        InstanceResourceAllocator.AllocationResult result2 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result2.getIndex()).getServerResource().setGpus(result2.getGpus());
        assertThat(result2.getIndex()).isEqualTo(1);
        for (int i=0; i < result1.getGpus().size(); i++) {
            assertThat(result1.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result2.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }
    }

    @Test
    public void allocateGPU_PASS_2_AND_FILL_UP_SERVERS1_2_3() {
        List<Server> list = getFreeGPUList();
        int requestGpuNum = MAX_GPU_NUM;
        InstanceResourceAllocator.AllocationResult result1 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result1.getIndex()).getServerResource().setGpus(result1.getGpus());
        InstanceResourceAllocator.AllocationResult result2 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result2.getIndex()).getServerResource().setGpus(result2.getGpus());
        InstanceResourceAllocator.AllocationResult result3 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result3.getIndex()).getServerResource().setGpus(result3.getGpus());
        assertThat(result3.getIndex()).isEqualTo(2);
        for (int i=0; i < result1.getGpus().size(); i++) {
            assertThat(result1.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result2.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result3.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }
    }

    @Test
    public void allocateGPU_PASS_2_AND_FILL_UP_SERVERS1_2_3_4() {
        List<Server> list = getFreeGPUList();
        int requestGpuNum = MAX_GPU_NUM;
        InstanceResourceAllocator.AllocationResult result1 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result1.getIndex()).getServerResource().setGpus(result1.getGpus());
        InstanceResourceAllocator.AllocationResult result2 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result2.getIndex()).getServerResource().setGpus(result1.getGpus());
        InstanceResourceAllocator.AllocationResult result3 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result3.getIndex()).getServerResource().setGpus(result1.getGpus());
        InstanceResourceAllocator.AllocationResult result4 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result4.getIndex()).getServerResource().setGpus(result1.getGpus());
        assertThat(result4.getIndex()).isEqualTo(3);
        for (int i=0; i < result1.getGpus().size(); i++) {
            assertThat(result1.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result2.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result3.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result4.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }
    }

    @Test
    public void allocateGPU_PASS_2_AND_FILL_UP_SERVERS1_2_3_4_5() {
        List<Server> list = getFreeGPUList();
        int requestGpuNum = MAX_GPU_NUM;
        InstanceResourceAllocator.AllocationResult result1 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result1.getIndex()).getServerResource().setGpus(result1.getGpus());
        InstanceResourceAllocator.AllocationResult result2 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result2.getIndex()).getServerResource().setGpus(result2.getGpus());
        InstanceResourceAllocator.AllocationResult result3 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result3.getIndex()).getServerResource().setGpus(result3.getGpus());
        InstanceResourceAllocator.AllocationResult result4 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result4.getIndex()).getServerResource().setGpus(result4.getGpus());
        InstanceResourceAllocator.AllocationResult result5 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result5.getIndex()).getServerResource().setGpus(result5.getGpus());
        assertThat(result5.getIndex()).isEqualTo(4);
        for (int i=0; i < result1.getGpus().size(); i++) {
            assertThat(result1.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result2.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result3.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result4.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result5.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }
    }

    @Test
    public void allocateGPU_PASS_2_AND_FILL_UP_SERVERS1_2_3_4_5_6() {
        List<Server> list = getFreeGPUList();
        int requestGpuNum = MAX_GPU_NUM;
        InstanceResourceAllocator.AllocationResult result1 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result1.getIndex()).getServerResource().setGpus(result1.getGpus());
        InstanceResourceAllocator.AllocationResult result2 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result2.getIndex()).getServerResource().setGpus(result2.getGpus());
        InstanceResourceAllocator.AllocationResult result3 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result3.getIndex()).getServerResource().setGpus(result3.getGpus());
        InstanceResourceAllocator.AllocationResult result4 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result4.getIndex()).getServerResource().setGpus(result4.getGpus());
        InstanceResourceAllocator.AllocationResult result5 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result5.getIndex()).getServerResource().setGpus(result5.getGpus());
        InstanceResourceAllocator.AllocationResult result6 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result6.getIndex()).getServerResource().setGpus(result5.getGpus());
        assertThat(result6.getIndex()).isEqualTo(5);
        for (int i=0; i < result1.getGpus().size(); i++) {
            assertThat(result1.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result2.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result3.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result4.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result5.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result6.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void allocateGPU_FAIL_WITH_EXCEPTION_WHEN_EVERY_SERVER_FILLED_UP_WITH_EXCLUSIVE_USE() {
        List<Server> list = getFreeGPUList();
        int requestGpuNum = MAX_GPU_NUM;
        InstanceResourceAllocator.AllocationResult result1 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result1.getIndex()).getServerResource().setGpus(result1.getGpus());
        InstanceResourceAllocator.AllocationResult result2 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result2.getIndex()).getServerResource().setGpus(result2.getGpus());
        InstanceResourceAllocator.AllocationResult result3 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result3.getIndex()).getServerResource().setGpus(result3.getGpus());
        InstanceResourceAllocator.AllocationResult result4 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result4.getIndex()).getServerResource().setGpus(result4.getGpus());
        InstanceResourceAllocator.AllocationResult result5 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result5.getIndex()).getServerResource().setGpus(result5.getGpus());
        InstanceResourceAllocator.AllocationResult result6 =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result6.getIndex()).getServerResource().setGpus(result5.getGpus());
        assertThat(result6.getIndex()).isEqualTo(5);
        for (int i=0; i < result1.getGpus().size(); i++) {
            assertThat(result1.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result2.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result3.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result4.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result5.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
            assertThat(result6.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }

        InstanceResourceAllocator.AllocationResult failedResult =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
    }

    @Test
    // 서버내에 독점적으로 사용하는 GPU 를 피해서 할당한다.
    public void allocateGPU_PASS_AND_OCCUPY_UN_ALLOCED_GPU() {
        List<Server> list = getFreeGPUList();
        int numExclusiveGPU = 3;
        InstanceResourceAllocator.AllocationResult result1 =
                instanceResourceAllocator.allocateGPU(list, numExclusiveGPU, true);
        assertThat(result1.getIndex()).isEqualTo(0);
        list.get(result1.getIndex()).getServerResource().setGpus(result1.getGpus());
        for (int i=0; i < numExclusiveGPU; i++) {
            assertThat(result1.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }
        int numSharedUseGPU = 3;
        InstanceResourceAllocator.AllocationResult result2 =
                instanceResourceAllocator.allocateGPU(list, numSharedUseGPU, false);
        assertThat(result2.getIndex()).isEqualTo(0);
        for (int i=numExclusiveGPU; i < 6; i++) {
            assertThat(result2.getGpus().get(i)).isEqualTo(ServerResource.ALLOC);
        }
    }

    @Test
    // 서버내에 독점적으로 사용하는 GPU 를 피해서 할당한다.
    public void allocateGPU_PASS_AND_OCCUPY_UN_ALLOCED_GPU_1() {
        List<Server> list = getFreeGPUList();
        int numExclusiveGPU = 3;
        InstanceResourceAllocator.AllocationResult result1 =
                instanceResourceAllocator.allocateGPU(list, numExclusiveGPU, true);
        assertThat(result1.getIndex()).isEqualTo(0);
        list.get(result1.getIndex()).getServerResource().setGpus(result1.getGpus());
        for (int i=0; i < numExclusiveGPU; i++) {
            assertThat(result1.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }
        int numSharedUseGPU = 5;
        InstanceResourceAllocator.AllocationResult result2 =
                instanceResourceAllocator.allocateGPU(list, numSharedUseGPU, false);
        assertThat(result2.getIndex()).isEqualTo(0);
        for (int i=numExclusiveGPU; i < result2.getGpus().size(); i++) {
            assertThat(result2.getGpus().get(i)).isEqualTo(ServerResource.ALLOC);
        }
    }

    @Test
    // 서버내의 GPU 를 한 번 씩 다 사용한 후에는, 다른 서버에서 탐색을 시작한다.
    public void allocateGPU_PASS_AND_OCCUPY_OTHER_SERVERS_GPU_TWICE_WHEN_NO_UN_ALLOC_GPU() {
        List<Server> list = getFreeGPUList();
        int numExclusiveGPU = 3;
        InstanceResourceAllocator.AllocationResult result1 =
                instan ceResourceAllocator.allocateGPU(list, numExclusiveGPU, true);
        assertThat(result1.getIndex()).isEqualTo(0);
        list.get(result1.getIndex()).getServerResource().setGpus(result1.getGpus());
        for (int i=0; i < numExclusiveGPU; i++) {
            assertThat(result1.getGpus().get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }
        int numSharedUseGPU = 6;
        InstanceResourceAllocator.AllocationResult result2 =
                instanceResourceAllocator.allocateGPU(list, numSharedUseGPU, false);
        assertThat(result2.getIndex()).isEqualTo(1);
        for (int i=0; i < numSharedUseGPU; i++) {
            assertThat(result2.getGpus().get(i)).isEqualTo(ServerResource.ALLOC);
        }
    }

    @Test
    // 모든 서버가 독점적이 아니면서, 한 번 씩은 할당된 상태라도 비 독점적 자원을 할당받을 수 있다
    // 단 할당하려는 갯수만큼의 GPU 가 최소 하나의 서버에서는 사용되지 않거나, 독점적이 아닌 상태로
    // 존재해야한다.
    public void allocateGPU_PASS_EVEN_EVERY_SERVER_IS_AT_LEAST_ONCE_OCCUPIED() {
        List<Server> list = getFreeGPUList();
        List<InstanceResourceAllocator.AllocationResult> resultList = new ArrayList<>();
        int requestGpuNum = MAX_GPU_NUM - 1;

        InstanceResourceAllocator.AllocationResult result =
                instanceResourceAllocator.allocateGPU(list, requestGpuNum, true);
        list.get(result.getIndex()).getServerResource().setGpus(result.getGpus());
        for (int i=0; i < 5; i++) {
            result = instanceResourceAllocator.allocateGPU(list, MAX_GPU_NUM, true);
            resultList.add(result);
            list.get(result.getIndex()).getServerResource().setGpus(result.getGpus());
            assertThat(result.getIndex()).isEqualTo(i+1);
        }

        InstanceResourceAllocator.AllocationResult pass1 =
                instanceResourceAllocator.allocateGPU(list, 1, false);
        assertThat(pass1.getIndex()).isEqualTo(0);
        assertThat(pass1.getGpus().get(MAX_GPU_NUM-1)).isEqualTo(ServerResource.ALLOC);
        list.get(pass1.getIndex()).getServerResource().setGpus(pass1.getGpus());
        InstanceResourceAllocator.AllocationResult pass2 =
                instanceResourceAllocator.allocateGPU(list, 1, false);
        assertThat(pass2.getIndex()).isEqualTo(0);
        assertThat(pass2.getGpus().get(MAX_GPU_NUM-1)).isEqualTo(ServerResource.ALLOC + 1);
    }




}
