package com.dna.rna.service.util;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.server.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class InstanceResourceAllocator {

    public static final int MAX_GPU_NUM = 8;

    @Getter
    @AllArgsConstructor
    public class AllocationResult {
        private final int index;
        private final List<Integer> gpus;
    }

    public AllocationResult allocateGPU(List<Server> serverList, int requestedGPU, boolean useResourceExclusively) {
        if (requestedGPU > MAX_GPU_NUM) {
            throw new IllegalArgumentException("한 인스턴스의 최대 사용 가능 GPU 개수는 " + MAX_GPU_NUM + "개 입니다.");
        }

        int accGpuNum = 0;
        List<Integer> result;

        for (int i = 0; i < serverList.size(); i++) {
            Server server = serverList.get(i);
            List<Integer> gpuStatuses = server.getServerResource().getGpus();
            result = new ArrayList<>(gpuStatuses);
            for (int j = 0; j < result.size(); j++) {
                int gpuStatus = result.get(j);
                if (gpuStatus == ServerResource.UN_ALLOC) {
                    if (useResourceExclusively) {
                        result.set(j, ServerResource.EXCLUSIVELY_ALLOC);
                    } else {
                        result.set(j, ServerResource.ALLOC);
                    }
                    accGpuNum++;
                }
                if (accGpuNum == requestedGPU) {
                    serverList.get(i).getServerResource().setGpus(result);
                    return new AllocationResult(i, result);
                }
            }
            accGpuNum = 0;
        }

        if (useResourceExclusively) {
            throw new IllegalArgumentException("배타적으로 사용할 수 있는 여분 GPU를 가진 서버가 없습니다.");
        }

        for (int i = 0; i < serverList.size(); i++) {
            Server server = serverList.get(i);
            List<Integer> gpuStatuses = server.getServerResource().getGpus();
            result = new ArrayList<>(gpuStatuses);
            for (int j = 0; j < result.size(); j++) {
                int gpuStatus = result.get(j);
                if (gpuStatus != ServerResource.EXCLUSIVELY_ALLOC) {
                    result.set(j, gpuStatus + 1);
                    accGpuNum++;
                }
                if (accGpuNum == requestedGPU) {
                    serverList.get(i).getServerResource().setGpus(result);
                    return new AllocationResult(i, result);
                }
            }
            accGpuNum = 0;
        }
        return null;
    }
}
