package com.dna.rna.domain.Board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static java.util.Objects.requireNonNull;

@Repository
public class BoardRepository {

    private static final Logger logger= LoggerFactory.getLogger(BoardRepository.class);

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void save(final Board board) throws DataIntegrityViolationException {
        requireNonNull(board, "Board는 null일 수 없습니다.");
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        em.persist(board);
    }
}
