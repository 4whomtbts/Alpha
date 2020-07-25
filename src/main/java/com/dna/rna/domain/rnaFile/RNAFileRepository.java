package com.dna.rna.domain.rnaFile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RNAFileRepository extends JpaRepository<RNAFile, Long>, CustomRNAFileRepository {
}
