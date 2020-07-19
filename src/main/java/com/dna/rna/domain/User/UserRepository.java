package com.dna.rna.domain.User;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Repository("UserRepository")
public class UserRepository {

    private static final Logger logger= LoggerFactory.getLogger(UserRepository.class);

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void save(final User user) throws DataIntegrityViolationException {
        requireNonNull(user, "User 은 null일 수 없습니다.");
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUser qUser = new QUser("qUser");
        List<User> exist = queryFactory.selectFrom(qUser).where(qUser.loginId.eq(user.getUserName())).fetch();

        if (exist.size() != 0) {
            if (exist.size() > 1) logger.error(String.format("심각 : loginId = [%s] 인 유저가 2개 이상 이미 존재합니다.", user.getLoginId()));
            throw new DataIntegrityViolationException(String.format("유저 loginId = [%s] 에 해당하는 유저가 이미 존재합니다.", user.getLoginId()));
        }
        em.persist(user);
    }

    @Transactional
    public User findUserByLoginId(final String loginId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUser qUser = new QUser("qUser");
        return queryFactory.selectFrom(qUser).where(qUser.loginId.eq(loginId)).fetchOne();
    }

}
