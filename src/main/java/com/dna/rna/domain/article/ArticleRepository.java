package com.dna.rna.domain.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, CustomArticleRepository {

    @Modifying
    @Transactional
    @Query("UPDATE Article a SET a.voteCount = a.voteCount + :vote WHERE a.articleId = :articleId")
    public void updateVoteCount(@Param("articleId") Long articleId, @Param("vote") int vote);

}
