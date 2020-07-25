package com.dna.rna.domain.rnaFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class RNAFileRepositoryImpl extends QuerydslRepositorySupport implements CustomRNAFileRepository {

    private static final Logger logger = LoggerFactory.getLogger(RNAFileRepositoryImpl.class);

    RNAFileRepositoryImpl() {
        super(RNAFile.class);
    }
}
