package com.dna.rna.domain.instanceLog;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class InstanceLogRepositoryImpl extends QuerydslRepositorySupport implements CustomInstanceLogRepository {

    public InstanceLogRepositoryImpl() {
        super(InstanceLogRepositoryImpl.class);
    }
}
