package com.dna.rna.domain.article.articleComment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long>, CustomArticleCommentRepository {
}
