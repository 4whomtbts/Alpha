package com.dna.rna.domain.user;

import org.springframework.dao.DataIntegrityViolationException;

public interface CustomUserRepository {
    public void save2(final User user) throws DataIntegrityViolationException;
}
