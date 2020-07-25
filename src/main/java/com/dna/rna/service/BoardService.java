package com.dna.rna.service;

import com.dna.rna.domain.article.Article;
import com.dna.rna.domain.article.ArticleRepository;
import com.dna.rna.domain.board.BoardRepository;
import com.dna.rna.dto.ArticleDto;
import com.dna.rna.service.util.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ArticleRepository articleRepository;

    public Page<ArticleDto.ListPreview> getArticlesByBoardId(long boardId, final PageRequest pageable) {
        Page<Article> fetchResult = articleRepository.fetchArticlesByBoardId(boardId, pageable);
        if (fetchResult != null) {
            return fetchResult.map(ArticleDto.ListPreview::new);
        }
        return null;
    }

}
