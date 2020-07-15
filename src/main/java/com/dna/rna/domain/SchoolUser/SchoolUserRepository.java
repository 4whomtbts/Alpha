package com.dna.rna.domain.SchoolUser;

import com.dna.rna.domain.School.QSchool;
import com.dna.rna.domain.School.School;
import com.dna.rna.domain.User.QUser;
import com.dna.rna.domain.User.User;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class SchoolUserRepository {

    @PersistenceContext
    EntityManager em;

    public void save(SchoolUser schoolUser) {
        em.persist(schoolUser);
    }

    @Transactional
    public List<SchoolUser> findByLoginIdAndSchoolId(String loginId, long schoolId) {


        JPAQuery query = new JPAQuery(em);
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSchoolUser qSchoolUser = new QSchoolUser("su");
        QSchool qSchool = new QSchool("s");
        QUser qUser = new QUser("u");
        School school = queryFactory.selectFrom(qSchool).where(qSchool.schoolName.eq("dongguk")).fetchOne();
        User user =  queryFactory.selectFrom(qUser).where(qUser.loginId.eq("4whomtbts")).fetchOne();
        //em.persist(user);
        SchoolUser schoolUser = SchoolUser.of(user, school, false);
        //em.persist(schoolUser);
        List<SchoolUser> schoolUsers =
                queryFactory.selectFrom(qSchoolUser)
                     .where(qSchoolUser.user.id.eq((long)4)
                            .and(qSchoolUser.school.id.eq((long)3))).fetch();

        //schoolUsers.forEach(System.out::println);
        System.out.println(schoolUsers.size());

        /*
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SchoolUser> query = cb.createQuery(SchoolUser.class);
        Root<SchoolUser> su = query.from(SchoolUser.class);

        CriteriaQuery<SchoolUser> cq = query.select(su).where(cb.equal(su.get))
        return em.createQuery("SELECT sm from SchoolUser sm WHERE sm.")
        */
        return schoolUsers;
    }
}
