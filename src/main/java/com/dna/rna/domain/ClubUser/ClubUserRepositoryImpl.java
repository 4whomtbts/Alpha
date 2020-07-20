package com.dna.rna.domain.ClubUser;

import com.dna.rna.domain.Board.BoardItem;
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

import static com.dna.rna.domain.ClubUser.QClubUser.clubUser;
import static java.util.Objects.requireNonNull;

@Repository
public class ClubUserRepositoryImpl extends QuerydslRepositorySupport implements CustomClubUserRepository {

    private static final Logger logger = LoggerFactory.getLogger(ClubUserRepositoryImpl.class);

    @PersistenceContext
    EntityManager em;

    ClubUserRepositoryImpl() {
        super(ClubUser.class);
    }

    @Transactional
    public void save(final ClubUser clubUser) {
        requireNonNull(clubUser, "ClubDto 은 null일 수 없습니다.");
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QClubUser qClubUser = QClubUser.clubUser;
        List<ClubUser> exist = queryFactory
                .selectFrom(qClubUser)
                .where(qClubUser.user.loginId.eq(clubUser.getUser().getLoginId())
                .and(qClubUser.club.id.eq(clubUser.getClub().getId()))).fetch();

        if (exist.size() != 0) {
            if (exist.size() > 1) logger.error(String.format("심각 : clubUserId = [%d]인 동아리가 2개 이상 이미 존재합니다.", clubUser.getId()));
            throw new DataIntegrityViolationException(String.format("clubUserId= [%d] 에 해당하는 동아리가 이미 존재합니다.", clubUser.getId()));
        }
        em.persist(clubUser);
    }

    // TODO BooleanExpression 검증
    @Transactional
    public boolean isUserMemberOfClub(final long clubId, final String loginId) throws IllegalArgumentException {
        if (clubId < 0) {
            IllegalArgumentException exception = new IllegalArgumentException("clubId 는 음수일 수 없습니다.");
            logger.error("clubId 는 음수일 수 없습니다.", exception);
            throw exception;
        }
        if (loginId == null || loginId.equals("")) {
            IllegalArgumentException exception = new IllegalArgumentException("loginId 는 null 혹은 빈 문자열일 수 없습니다.");
            logger.error("심각 : loginId 는 null 혹은 빈 문자열일 수 없습니다.", exception);
            throw exception;
        }
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QClubUser qClubUser = clubUser;

        List<ClubUser> exist = queryFactory
                .selectFrom(qClubUser)
                .where(qClubUser.user.loginId.eq(loginId)
                        .and(qClubUser.club.id.eq(clubId))).fetch();
        return (exist.size() > 0);
    }

    @Transactional
    public List<BoardItem> fetchBoards(final long clubId) {
        return null;
    }

}
