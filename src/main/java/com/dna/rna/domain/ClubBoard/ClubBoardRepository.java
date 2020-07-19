package com.dna.rna.domain.ClubBoard;

import com.dna.rna.domain.Board.Board;
import com.dna.rna.domain.Club.Club;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sun.istack.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Repository
public class ClubBoardRepository {

    private static final Logger logger= LoggerFactory.getLogger(ClubBoardRepository.class);

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void save(ClubBoard clubBoard) {
        requireNonNull(clubBoard, "clubBoard는 null이 될 수 없습니다");
        em.persist(clubBoard);
    }

    @Nullable
    @Transactional
    public List<ClubBoard> fetchBoardsOfClub(long clubId, long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QClubBoard qClubBoard = QClubBoard.clubBoard;
        return queryFactory.selectFrom(qClubBoard)
                .where(qClubBoard.club.id.eq(clubId)
                        .and(qClubBoard.board.id.eq(boardId))).fetch();
    }

    @Nullable
    @Transactional
    public List<ClubBoard> fetchBoardsOfClub(Club club, Board board) {
        requireNonNull(club, "Club 은 null일 수 없습니다.");
        requireNonNull(board, "Board는 null일 수 없습니다.");
        return fetchBoardsOfClub(club.getId(), board.getId());
    }



}
