package com.dna.rna.dto;

import com.dna.rna.domain.article.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ListPreview {
        private long articleId;
        private String title;
        private String author;
        private LocalDateTime createdAt;

        public ListPreview(Article article) {
            this.articleId = article.getArticleId();
            this.title = article.getTitle();
            this.author = article.getAuthor().getUserName();
            this.createdAt = article.getCreatedAt();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResArticle {
        private long articleId;
        private UserDto.ResAuthor author;
        private String title;
        private String content;
        private int viewCount;
        private int voteCount;
        private List<ArticleCommentDto.ResAsArticleItem> comments;
        private LocalDateTime createdAt;
    }

}
