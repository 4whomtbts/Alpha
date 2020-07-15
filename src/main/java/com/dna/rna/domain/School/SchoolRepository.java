package com.dna.rna.domain.School;

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

@Repository
public class SchoolRepository {

    private static final Logger logger= LoggerFactory.getLogger(SchoolRepository.class);

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void save(final School school) throws DataIntegrityViolationException {
        requireNonNull(school, "School 은 null일 수 없습니다.");
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSchool qSchool = new QSchool("qUser");
        List<School> exist = queryFactory.selectFrom(qSchool).where(qSchool.schoolName.eq(school.getSchoolName())).fetch();

        if (exist.size() != 0) {
            if (exist.size() > 1) logger.error(String.format("심각 : 학교 이름이 [%s]인 학교 엔터티가 이미 두 개 이상 존재합니다.", school.getSchoolName()));
            throw new DataIntegrityViolationException(String.format("학교 이름 [%s] 에 해당하는 학교가 이미 존재합니다.", school.getSchoolName()));
        }
        em.persist(school);
    }

    @Transactional
    public School findBySchoolName(final String schoolName) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSchool qSchool = new QSchool("qUser");
        return queryFactory.selectFrom(qSchool).where(qSchool.schoolName.eq(schoolName)).fetchOne();
    }
}
