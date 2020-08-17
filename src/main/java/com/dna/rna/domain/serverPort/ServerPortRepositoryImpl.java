package com.dna.rna.domain.serverPort;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ServerPortRepositoryImpl extends QuerydslRepositorySupport implements CustomServerPortRepository {

    public ServerPortRepositoryImpl() {
        super(ServerPortRepositoryImpl.class);
    }

}
