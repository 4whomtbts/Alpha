package com.dna.rna.controller;

import com.dna.rna.domain.article.Article;
import com.dna.rna.domain.article.ArticleRepository;
import com.dna.rna.domain.article.articleComment.ArticleComment;
import com.dna.rna.dto.ArticleCommentDto;
import com.dna.rna.dto.ArticleDto;
import com.dna.rna.dto.UserDto;
import com.dna.rna.exception.RnaException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "게시글 컨트롤러", value = "article 컨트롤러 v1")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleRepository articleRepository;

    @Transactional
    @ApiOperation(value = "게시글 조회", notes = "articleId 를 이용해서 게시글을 조회한다.")
    @GetMapping("/articles/article/{articleId}")
    public ArticleDto.ResArticle getArticle(
            @ApiParam(name = "articleId", value = "게시글 id", required = true)
            @PathVariable("articleId")
                    long articleId) {

        if (articleId < 0) {
            throw RnaException.ofIllegalArgumentException("articleId가 올바른 값이 아닌 [" + articleId + "] 였습니다.", null);
        }

        Article article = articleRepository.findById(articleId).orElse(null);

        if (article == null) {
            return null;
        }

        article.increaseViewCount();
        articleRepository.save(article);

        List<ArticleCommentDto.ResAsArticleItem> comments = new ArrayList<>();
        for (ArticleComment comment : article.getComments()) {
            comments.add(ArticleCommentDto.ResAsArticleItem.ofArticleComment(comment));
        }
        return new ArticleDto.ResArticle(
                article.getArticleId(), new UserDto.ResAuthor(article.getAuthor()), article.getTitle(),
                article.getContent(), article.getViewCount(), article.getVoteCount(), comments,
                article.getCreatedAt());
    }

    //TODO User 확인해서 중복추천 금지
    @ApiOperation(value = "게시글 추천", notes = "articleId 를 이용해서 게시글을 한 번 추천한다. 총 추천수는 음수도 가능하다.")
    @PutMapping("/articles/article/{articleId}/upvote")
    public ResponseEntity upvoteArticle(
            @ApiParam(name = "articleId", value = "게시글 id", required = true)
            @PathVariable("articleId")
            long articleId) {

        if (articleId < 0) {
            throw RnaException.ofIllegalArgumentException("articleId가 올바른 값이 아닌 [" + articleId + "] 였습니다.", null);
        }

        articleRepository.updateVoteCount(articleId, 1);
        return new ResponseEntity(HttpStatus.OK);
    }

    //TODO User 확인해서 중복 비추천 금지
    @ApiOperation(value = "게시글 비추천", notes = "articleId 를 이용해서 게시글을 한 번 비추천한다. 총 추천수는 음수도 가능하다.")
    @PutMapping("/articles/article/{articleId}/downvote")
    public ResponseEntity downvoteArticle(
            @ApiParam(name = "articleId", value = "게시글 id", required = true)
            @PathVariable("articleId")
            long articleId) {

        if (articleId < 0) {
            throw RnaException.ofIllegalArgumentException("articleId가 올바른 값이 아닌 [" + articleId + "] 였습니다.", null);
        }

        articleRepository.updateVoteCount(articleId, -1);
        return new ResponseEntity(HttpStatus.OK);
    }
}
