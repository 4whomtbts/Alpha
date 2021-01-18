package com.dna.rna.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Repository
public class UserRepositoryImpl extends QuerydslRepositorySupport implements CustomUserRepository {

    private static final Logger logger= LoggerFactory.getLogger(UserRepositoryImpl.class);

    @PersistenceContext
    EntityManager em;

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Transactional
    public void save(final User user) throws DataIntegrityViolationException {
        requireNonNull(user, "user 은 null일 수 없습니다.");
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUser qUser = new QUser("qUser");
        List<User> exist = queryFactory.selectFrom(qUser).where(qUser.loginId.eq(user.getUserName())).fetch();

        if (exist.size() != 0) {
            if (exist.size() > 1) logger.error(String.format("심각 : loginId = [%s] 인 유저가 2개 이상 이미 존재합니다.", user.getLoginId()));
            throw new DataIntegrityViolationException(String.format("유저 loginId = [%s] 에 해당하는 유저가 이미 존재합니다.", user.getLoginId()));
        }
        em.persist(user);
    }

}
