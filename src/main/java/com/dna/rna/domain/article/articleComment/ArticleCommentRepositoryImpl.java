package com.dna.rna.domain.article.articleComment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleCommentRepositoryImpl extends QuerydslRepositorySupport implements  CustomArticleCommentRepository{

    private static final Logger logger = LoggerFactory.getLogger(ArticleCommentRepositoryImpl.class);

    ArticleCommentRepositoryImpl() {
        super(ArticleComment.class);
    }

}
