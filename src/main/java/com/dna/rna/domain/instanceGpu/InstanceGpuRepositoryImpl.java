package com.dna.rna.domain.instanceGpu;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class InstanceGpuRepositoryImpl extends QuerydslRepositorySupport implements CustomInstanceGpuRepository {

    public InstanceGpuRepositoryImpl() {
        super(InstanceGpuRepositoryImpl.class);
    }
}
