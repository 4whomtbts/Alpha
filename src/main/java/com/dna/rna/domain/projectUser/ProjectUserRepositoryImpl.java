package com.dna.rna.domain.projectUser;

import com.dna.rna.domain.Project.CustomProjectRepository;
import com.dna.rna.domain.Project.ProjectRepositoryImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProjectUserRepositoryImpl extends QuerydslRepositorySupport implements CustomProjectRepository {

    @PersistenceContext
    private EntityManager em;

    ProjectUserRepositoryImpl() {
        super(ProjectUser.class);
    }

}
