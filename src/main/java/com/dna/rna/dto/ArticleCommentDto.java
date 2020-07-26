package com.dna.rna.dto;

import com.dna.rna.domain.article.articleComment.ArticleComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ArticleCommentDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ResAsArticleItem {
        private long articleCommentId;
        private UserDto.ResAuthor author;
        private String content;
        private boolean deleted;
        private List<ResAsArticleItem> nestedComments;

        public static ResAsArticleItem ofArticleComment(ArticleComment comment) {
            List<ResAsArticleItem> nestedComments = new ArrayList<>();
            if (comment.getNestedComments() != null) {
                for (int i=0; i < comment.getNestedComments().size(); i++) {
                    ArticleComment curr = comment.getNestedComments().get(i);
                    nestedComments.add(ResAsArticleItem.ofArticleComment(curr));
                }
            }
            return new ResAsArticleItem(
                    comment.getArticleCommentId(), new UserDto.ResAuthor(comment.getAuthor()),
                    comment.getContent(), comment.isDeleted(), nestedComments);
        }
    }

    public static class ResAsArictleList {

    }
}
