package com.dna.rna.domain.server;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long>, CustomServerRepository {
}
