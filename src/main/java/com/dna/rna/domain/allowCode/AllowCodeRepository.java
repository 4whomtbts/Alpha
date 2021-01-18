package com.dna.rna.domain.allowCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllowCodeRepository extends JpaRepository<AllowCode, Long>, CustomAllowCodeRepository {

    public AllowCode findByAllowCode(String allowCode);
}
