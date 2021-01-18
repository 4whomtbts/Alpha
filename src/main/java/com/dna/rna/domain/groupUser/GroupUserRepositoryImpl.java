package com.dna.rna.domain.groupUser;

import com.dna.rna.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class GroupUserRepositoryImpl extends QuerydslRepositorySupport implements CustomGroupUserRepository {

    @PersistenceContext
    private EntityManager em;

    public GroupUserRepositoryImpl() {
        super(GroupUserRepositoryImpl.class);
    }

    @Override
    public List<GroupUser> findByUser(User user) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QGroupUser qGroupUser = QGroupUser.groupUser;
        return queryFactory.selectFrom(qGroupUser)
                    .where(qGroupUser.user.eq(user))
                    .fetch();
    }
}
