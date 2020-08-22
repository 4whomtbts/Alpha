package com.dna.rna.domain.group;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class GroupRepositoryImpl extends QuerydslRepositorySupport implements CustomGroupRepository {

    public GroupRepositoryImpl() {
        super(GroupRepositoryImpl.class);
    }
}
