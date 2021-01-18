package com.dna.rna.domain.server;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ServerRepositoryImpl extends QuerydslRepositorySupport implements CustomServerRepository {

    public ServerRepositoryImpl() {
        super(ServerRepositoryImpl.class);
    }
}
