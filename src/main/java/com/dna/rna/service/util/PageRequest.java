package com.dna.rna.service.util;

import lombok.Getter;
import org.springframework.data.domain.Sort;

// 참고 : https://cheese10yun.github.io/spring-jpa-best-12/
@Getter
public final class PageRequest {

    private int page = 0;
    private int size = 20;
    private String sort = "createdAt";
    private Sort.Direction direction = Sort.Direction.ASC;

    public void setSort(String sort) {
        if (sort == null || sort.equals("")) {
            this.sort = "createdAt";
        }

        this.sort = sort;
    }

    public void setPage(int page) {
        this.page = page <= 0 ? 1: page;
    }

    public void setSize(int size) {
        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 50;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setDirection(Sort.Direction direction) {
        if (direction == null) direction = Sort.Direction.ASC;
        this.direction = direction;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, Sort.by(direction, sort));
    }
}
