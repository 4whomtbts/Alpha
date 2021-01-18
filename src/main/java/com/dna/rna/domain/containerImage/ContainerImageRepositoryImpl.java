package com.dna.rna.domain.containerImage;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ContainerImageRepositoryImpl extends QuerydslRepositorySupport implements CustomContainerImageRepository {

    public ContainerImageRepositoryImpl() {
        super(ContainerImageRepositoryImpl.class);
    }
}
