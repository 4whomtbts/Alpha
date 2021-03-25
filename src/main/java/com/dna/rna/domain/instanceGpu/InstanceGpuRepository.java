package com.dna.rna.domain.instanceGpu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceGpuRepository extends JpaRepository<InstanceGpu, Long>, CustomInstanceGpuRepository {
}
