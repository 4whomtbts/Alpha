package com.dna.rna.controller;

import com.dna.rna.domain.article.ArticleRepository;
import com.dna.rna.domain.board.BoardRepository;
import com.dna.rna.dto.ArticleDto;
import com.dna.rna.service.BoardService;
import com.dna.rna.service.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value ="board 컨트롤러 v1")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;

    // 참고 : https://cheese10yun.github.io/spring-jpa-best-12/
    @ApiOperation(
            value = "게시판 아이디(boardId)를 이용해서 해당 게시판의 글들의 리스트를 가져온다.",
            notes = "파라미터가 없을 시 [0페이지, 20개, createdAt 기준, 오름차순] 이다.\n" +
                    "입력예시 = page=1&size=30&sort=articleId&direction=DESC \n" +
                    "          page=1&sort=createdAt&direction=DESC\n" +
                    "          page=1&direction=ASC\n" +
                    "          page=1")
    @GetMapping("/boards/board/{boardId}/articles")
    public Page<ArticleDto.ListPreview> getArticlesByBoard(
            @PathVariable("boardId") long boardId,
            final PageRequest pageable) {
        return boardService.getArticlesByBoardId(boardId, pageable);
    }

}
