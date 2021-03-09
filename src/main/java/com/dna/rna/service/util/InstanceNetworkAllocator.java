package com.dna.rna.service.util;

import com.dna.rna.domain.instance.InstanceNetworkSetting;

import java.util.Map;


public class InstanceNetworkAllocator {

    public InstanceNetworkSetting allocateNetwork(Map<String, Integer> externalPorts, Map<String, Integer> internalPorts) {
        return new InstanceNetworkSetting(externalPorts, internalPorts);
    }
}
