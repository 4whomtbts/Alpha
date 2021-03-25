package com.dna.rna.domain.gpu;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class GpuRepositoryImpl extends QuerydslRepositorySupport implements CustomGpuRepository {

    public GpuRepositoryImpl() {
        super(GpuRepositoryImpl.class);
    }
}
