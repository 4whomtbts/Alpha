package com.dna.rna.domain.article;

import com.dna.rna.service.util.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomArticleRepository {

    public Page<Article> fetchArticlesByBoardId(long boardId, PageRequest pageable);

}
