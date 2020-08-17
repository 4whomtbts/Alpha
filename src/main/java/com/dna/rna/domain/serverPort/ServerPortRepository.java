package com.dna.rna.domain.serverPort;

import com.dna.rna.domain.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerPortRepository extends JpaRepository<ServerPort, Long>, CustomServerPortRepository {

    public ServerPort findFirstByServerOrderByPortDesc(Server server);
}
