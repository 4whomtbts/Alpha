package com.dna.rna.domain.article;

import com.dna.rna.service.util.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomArticleRepository {

    public Page<Article> fetchArticlesByBoardId(long boardId, PageRequest pageable);

}
