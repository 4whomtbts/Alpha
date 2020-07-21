package com.dna.rna.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static java.util.Objects.requireNonNull;

@Repository
public class BoardRepositoryImpl extends QuerydslRepositorySupport
    implements CustomBoardRepository {

    private static final Logger logger= LoggerFactory.getLogger(BoardRepositoryImpl.class);

    @PersistenceContext
    EntityManager em;

    BoardRepositoryImpl() {
        super(Board.class);
    }

    @Transactional
    public Board save(final Board board) throws DataIntegrityViolationException {
        requireNonNull(board, "Board는 null일 수 없습니다.");
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        em.persist(board);
        em.flush();
        return board;
    }
}
