package com.dna.rna.domain.article;

import com.dna.rna.dto.ArticleDto;
import com.dna.rna.service.util.PageRequest;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("ArticleRepository")
public class ArticleRepositoryImpl extends QuerydslRepositorySupport implements CustomArticleRepository {

    private static final Logger logger = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

    @PersistenceContext
    EntityManager em;

    ArticleRepositoryImpl() {
        super(Article.class);
    }


    public List<ArticleDto.ListPreview> getArticlesByBoardId(long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QArticle qArticle = QArticle.article;

        /*
        List<ArticleDto.ListPreview> articles =
                queryFactory.selectFrom(qArticle)
                            .where(qArticle.board.id.eq(boardId)).fetchAll();
        */
        return null;
    }

    public Page<Article> fetchArticlesByBoardId(long boardId, PageRequest pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QArticle qArticle = QArticle.article;
        OrderSpecifier orderSpecifier;

        switch (pageable.getSort()) {
            case "articleId":
                orderSpecifier = qArticle.articleId.asc();
                if (pageable.getDirection() == Sort.Direction.DESC) {
                    orderSpecifier = qArticle.articleId.desc();
                }
                break;

            default:
                orderSpecifier = qArticle.createdAt.asc();
                if (pageable.getDirection() == Sort.Direction.DESC) {
                    orderSpecifier = qArticle.createdAt.desc();
                }
        }

        // 참고 : https://joont92.github.io/jpa/QueryDSL/
        QueryResults<Article> result =
                queryFactory.selectFrom(qArticle)
                        .where(qArticle.board.id.eq(boardId))
                        .offset(pageable.of().getOffset())
                        .limit(pageable.of().getPageSize())
                        .orderBy(orderSpecifier)
                        .fetchResults();

        return new PageImpl<>(result.getResults(), pageable.of(), result.getTotal());
    }
}
