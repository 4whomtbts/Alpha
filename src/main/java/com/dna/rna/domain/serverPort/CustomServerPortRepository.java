package com.dna.rna.domain.serverPort;

import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.instanceGpu.InstanceGpu;
import com.dna.rna.domain.server.Server;

import java.util.List;

public interface CustomServerPortRepository {

    public List<ServerPort> fetchFreeExternalPortOfServer(Server server);

    public List<ServerPort> fetchFreeInternalPortOfServer(Server server);

    public void removeServerPortByInstance(Instance instance);

}
