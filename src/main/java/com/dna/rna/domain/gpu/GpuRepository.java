package com.dna.rna.domain.gpu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GpuRepository extends JpaRepository<Gpu, Long>, CustomGpuRepository {
}
