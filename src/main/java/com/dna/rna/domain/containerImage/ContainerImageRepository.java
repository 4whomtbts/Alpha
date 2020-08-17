package com.dna.rna.domain.containerImage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainerImageRepository extends JpaRepository<ContainerImage, Long>, CustomContainerImageRepository {
}
