package com.dna.rna.domain.serverPort;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerPortRepository extends JpaRepository<ServerPort, Long>, CustomServerPortRepository {


}
