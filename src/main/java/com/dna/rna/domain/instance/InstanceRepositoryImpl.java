package com.dna.rna.domain.instance;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class InstanceRepositoryImpl extends QuerydslRepositorySupport implements CustomInstanceRepository {

    public InstanceRepositoryImpl() {
        super(InstanceRepositoryImpl.class);
    }

}
