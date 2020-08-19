package com.dna.rna.domain.serverPort;

import com.dna.rna.domain.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerPortRepository extends JpaRepository<ServerPort, Long>, CustomServerPortRepository {


}
