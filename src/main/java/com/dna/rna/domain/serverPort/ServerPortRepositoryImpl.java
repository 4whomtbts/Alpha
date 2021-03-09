package com.dna.rna.domain.serverPort;

import com.dna.rna.domain.server.Server;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ServerPortRepositoryImpl extends QuerydslRepositorySupport implements CustomServerPortRepository {

    @PersistenceContext
    EntityManager em;

    public ServerPortRepositoryImpl() {
        super(ServerPortRepositoryImpl.class);
    }

    public List<ServerPort> fetchFreeExternalPortOfServer(Server server) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QServerPort qServerPort = QServerPort.serverPort;
        OrderSpecifier orderSpecifier = qServerPort.from.asc();

        List<ServerPort> result = queryFactory.selectFrom(qServerPort)
                            .where(qServerPort.instance.server.serverId.eq(server.getServerId())
                                    .and(qServerPort.external.eq(true)))
                            .orderBy(orderSpecifier).fetch();
        return result;
    }

    public List<ServerPort> fetchFreeInternalPortOfServer(Server server) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QServerPort qServerPort = QServerPort.serverPort;
        OrderSpecifier orderSpecifier = qServerPort.from.asc();

        List<ServerPort> result = queryFactory.selectFrom(qServerPort)
                .where(qServerPort.instance.server.eq(server)
                        .and(qServerPort.external.eq(false)))
                .orderBy(orderSpecifier).fetch();
        return result;
    }


}
