package com.dna.rna.domain.instance;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class InstanceRepositoryImpl extends QuerydslRepositorySupport implements CustomInstanceRepository {

    @PersistenceContext
    EntityManager em;

    public InstanceRepositoryImpl() {
        super(InstanceRepositoryImpl.class);
    }

    @Override
    public List<Instance> findByUserLoginId(String loginId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QInstance qInstance = QInstance.instance;

        return queryFactory.selectFrom(qInstance)
                           .where(qInstance.owner.loginId.eq(loginId))
                           .fetch();
    }
}
