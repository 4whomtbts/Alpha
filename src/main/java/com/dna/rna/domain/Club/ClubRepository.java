package com.dna.rna.domain.Club;

import com.dna.rna.domain.ClubUser.ClubUserRepository;
import com.dna.rna.domain.User.QUser;
import com.dna.rna.domain.User.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
public class ClubRepository {

    private static final Logger logger= LoggerFactory.getLogger(ClubRepository.class);

    @PersistenceContext
    EntityManager em;

    private final ClubUserRepository clubUserRepository;

    @Transactional
    public void save(final Club club) throws DataIntegrityViolationException {
        requireNonNull(club, "Club 은 null일 수 없습니다.");
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QClub qClub = new QClub("qClub");
        List<Club> exist = queryFactory.selectFrom(qClub).where(qClub.clubName.eq(club.getClubName())).fetch();

        if (exist.size() != 0) {
            if (exist.size() > 1) logger.error(String.format("심각 : clubName = [%s]인 동아리가 2개 이상 이미 존재합니다.", club.getClubName()));
            throw new DataIntegrityViolationException(String.format("동아리 명 clubName = [%s] 에 해당하는 동아리가 이미 존재합니다.", club.getClubName()));
        }
        em.persist(club);
    }

    @Transactional
    public Club findByClubName(final String clubName) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QClub qClub = new QClub("qClub");
        return queryFactory.selectFrom(qClub).where(qClub.clubName.eq(clubName)).fetchOne();
    }

    @Transactional
    public User fetchLeader(final long clubId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QClub qClub = new QClub("qClub");
        Club club = queryFactory.selectFrom(qClub).where(qClub.id.eq(clubId)).fetchOne();
        if (club == null) {
            final String error = String.format("동아리 아이디 clubId = [%s] 에 해당하는 동아리가 존재하지 않습니다.", clubId);
            logger.error("심각 : clubId = [%d]인 존재하지 않는 동아리로 fetchLeader를 호출했음");
            throw new DataIntegrityViolationException(error);
        }

        return club.getLeader();
    }

    // TODO ClubUserRepository 조회해서 해당 유저가 동아리의 멤버인지 확인하기
    // 유저를 동아리 장으로 삼는다.
    @Transactional
    public boolean grantUserLeaderRole(final long clubId, final String loginId) throws IllegalArgumentException {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QClub qClub = QClub.club;
        QUser qUser = QUser.user;
        User leader = queryFactory.selectFrom(qUser).where(qUser.loginId.eq(loginId)).fetchOne();
        if (leader == null) {
            final String error = String.format("loginId = [%s] 인 유저가 존재하지 않습니다.", loginId);
            logger.error("심각 : loginId = [%s]인 존재하지 않은 유저에 대해 grantUserAsClubLeader를 실행했음");
        }
        if (!clubUserRepository.isUserMemberOfClub(clubId, loginId)) {
            IllegalArgumentException exception =
                    new IllegalArgumentException(
                            String.format("clubId = [%d], loginId = [%s] 인 clubUser가 존재하지 않습니다..",
                                    clubId, loginId));
            logger.error("심각 : clubId = [%d], loginId = [%s] 인 clubUser가 존재하지 않습니다..", exception);
            throw exception;
        }

        long affected = queryFactory.update(qClub).where(qClub.id.eq(clubId)).set(qClub.leader, leader).execute();
        // 영향받은 club 이 0보다 작은 경우, club 이 존재하지 않거나, 조건에 맞는 club이 없는 경우이므로 실패
        if (affected < 0) {
            return false;
        }

        em.clear();
        return true;
    }

    @Transactional
    public long fetchCurrentSeasonUserCount(final long clubId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QClubUser qClubUser = QClubUser.clubUser;
        QClub qClub = QClub.club;
        String currSeason = queryFactory.select(qClub.season).where(qClub.id.eq(clubId)).from(qClub).fetchOne();
        if (currSeason != null) {
            return queryFactory.selectFrom(qClubUser)
                    .where(qClubUser.club.id.eq(clubId)
                            .and(qClubUser.joinSeason.eq(currSeason))).fetchCount();
        }
        return -1;
    }

}