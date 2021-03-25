package com.dna.rna.service.util;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.gpu.Gpu;
import com.dna.rna.domain.instanceGpu.InstanceGpu;
import com.dna.rna.domain.server.Server;
import com.dna.rna.exception.DCloudException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InstanceResourceAllocator {

    private static final Logger logger = LoggerFactory.getLogger(InstanceResourceAllocator.class);

    public static final int MAX_GPU_NUM = 8;

    @Getter
    @AllArgsConstructor
    public class AllocationResult {
        private final int index;
        private final List<Integer> gpus;
        private final List<Integer> newInstanceGpus;
    }

    @Getter
    @AllArgsConstructor
    public class AllocResult {
        private final Server server;
        private final List<InstanceGpu> instanceGpuList;
    }

    public AllocationResult allocateGPU(List<Server> serverList, int requestedGPU, boolean useResourceExclusively) {
        if (requestedGPU > MAX_GPU_NUM) {
            throw new IllegalArgumentException("한 인스턴스의 최대 사용 가능 Gpu 개수는 " + MAX_GPU_NUM + "개 입니다.");
        }

        int accGpuNum = 0;
        List<Integer> result;
        List<Integer> newInstanceGpus = new ArrayList<Integer>(Collections.nCopies(MAX_GPU_NUM, 0));

        for (int i = 0; i < serverList.size(); i++) {
            Server server = serverList.get(i);
            List<Integer> gpuStatuses = server.getServerResource().getGpus();
            result = new ArrayList<>(gpuStatuses);
            for (int j = 0; j < result.size(); j++) {
                int gpuStatus = result.get(j);
                if (gpuStatus == ServerResource.UN_ALLOC) {
                    if (useResourceExclusively) {
                        result.set(j, ServerResource.EXCLUSIVELY_ALLOC);
                        newInstanceGpus.set(j, ServerResource.EXCLUSIVELY_ALLOC);
                    } else {
                        result.set(j, ServerResource.ALLOC);
                        newInstanceGpus.set(j, ServerResource.ALLOC);
                    }
                    accGpuNum++;
                }
                if (accGpuNum == requestedGPU) {
                    return new AllocationResult(i, result, newInstanceGpus);
                }
            }
            accGpuNum = 0;
        }

        if (useResourceExclusively) {
            throw new IllegalArgumentException("배타적으로 사용할 수 있는 여분 GPU를 가진 서버가 없습니다.");
        }

        serverList.sort((o1, o2) -> {
            int o1InstanceSize = o1.getInstanceList().size();
            int o2InstanceSize = o2.getInstanceList().size();
            return Integer.compare(o1InstanceSize, o2InstanceSize);
        });
        for (int i = 0; i < serverList.size(); i++) {
            Server server = serverList.get(i);
            List<Integer> gpuStatuses = server.getServerResource().getGpus();

            result = new ArrayList<>(gpuStatuses);
            newInstanceGpus = new ArrayList<Integer>(Collections.nCopies(MAX_GPU_NUM, 0));
            for (int j = 0; j < result.size(); j++) {
                int gpuStatus = result.get(j);
                    if (gpuStatus != ServerResource.EXCLUSIVELY_ALLOC) {
                        result.set(j, gpuStatus + 1);
                    newInstanceGpus.set(j, ServerResource.ALLOC);
                    accGpuNum++;
                }
                if (accGpuNum == requestedGPU) {
                    return new AllocationResult(i, result, newInstanceGpus);
                }
            }
            accGpuNum = 0;
        }

        logger.error("매우심각 : allocateGPU 에 실패함 => 요청 GPU개수 : {}, 독점여부 : {}", requestedGPU);
        throw DCloudException.ofInternalServerError("매우심각 : allocateGPU 에 실패함.");
    }

    public AllocResult allocGpu(List<Server> serverList, int requestedGpu, boolean occupyExclusively) {
        serverList.sort((s1, s2) -> {
            long o1Time = s1.getLastInstanceAllocationTime();
            long o2Time = s2.getLastInstanceAllocationTime();
            return Long.compare(o1Time, o2Time);
        });
        if (occupyExclusively) {
            for (Server server : serverList) {
                boolean enoughGpuFound = false;
                List<InstanceGpu> createdInstanceGpuList = new ArrayList<>();
                for (Gpu gpu : server.getGpuList()) {
                    List<InstanceGpu> instanceGpuList = gpu.getInstanceGpuList();
                    if (instanceGpuList.size() == 0) {
                        createdInstanceGpuList.add(new InstanceGpu(server, gpu, true));
                    }
                    if (createdInstanceGpuList.size() == requestedGpu) {
                        enoughGpuFound = true;
                        break;
                    }
                }
                if (enoughGpuFound) {
                    server.setLastInstanceAllocationTime(System.currentTimeMillis());
                    return new AllocResult(server, createdInstanceGpuList);
                }
            }
            throw new IllegalArgumentException("배타적으로 사용할 수 있는 여분 GPU를 가진 서버가 없습니다.");
        }

        for (Server server : serverList) {
            boolean enoughGpuFound = false;
            List<InstanceGpu> createdInstanceGpuList = new ArrayList<>();
            List<Gpu> gpuList = server.getGpuList();
            gpuList.sort(Comparator.comparingInt(g -> g.getInstanceGpuList().size()));
            for (Gpu gpu : gpuList) {
                boolean exclusivelyOccupiedGpu = false;
                List<InstanceGpu> instanceGpuList = gpu.getInstanceGpuList();
                for (InstanceGpu instanceGpu : instanceGpuList) {
                    if (instanceGpu.isExclusivelyOccupied()) {
                        exclusivelyOccupiedGpu = true;
                        break;
                    }
                }
                if (exclusivelyOccupiedGpu) continue;
                createdInstanceGpuList.add(new InstanceGpu(server, gpu, false));
                if (createdInstanceGpuList.size() == requestedGpu) {
                    server.setLastInstanceAllocationTime(System.currentTimeMillis());
                    enoughGpuFound = true;
                    break;
                }
            }
            if (enoughGpuFound) {
                return new AllocResult(server, createdInstanceGpuList);
            } else {
                throw new IllegalArgumentException("배타적으로 사용할 수 있는 여분 GPU를 가진 서버가 없습니다.");
            }
        }
        logger.error("매우심각 : allocateGPU 에 실패함 => 요청 GPU개수 : {}, 독점여부 : {}", requestedGpu);
        throw DCloudException.ofInternalServerError("매우심각 : allocateGPU 에 실패함.");
    }
}
