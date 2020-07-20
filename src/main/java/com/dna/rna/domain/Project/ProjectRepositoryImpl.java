package com.dna.rna.domain.Project;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ProjectRepositoryImpl extends QuerydslRepositorySupport implements CustomProjectRepository {

    @PersistenceContext
    private EntityManager em;

    ProjectRepositoryImpl() {
        super(Project.class);
    }

}
