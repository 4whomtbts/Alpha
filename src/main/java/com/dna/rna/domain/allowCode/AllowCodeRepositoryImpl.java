package com.dna.rna.domain.allowCode;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AllowCodeRepositoryImpl extends QuerydslRepositorySupport implements CustomAllowCodeRepository {

    private static final Logger logger = LoggerFactory.getLogger(AllowCodeRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public AllowCodeRepositoryImpl() {
        super(AllowCodeRepositoryImpl.class);
    }

    @Override
    public AllowCode findUnExpiredAllowCode() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAllowCode qAllowCode = QAllowCode.allowCode1;
        return queryFactory.selectFrom(qAllowCode)
                           .where(qAllowCode.expired.eq(false))
                           .orderBy(qAllowCode.allowCodeId.asc()).fetchFirst();
    }
}
