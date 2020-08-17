package com.dna.rna.domain.instance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.HashMap;
import java.util.Map;

@Embeddable
@AllArgsConstructor
@Setter
@Getter
public class InstanceNetworkSetting {

    @ElementCollection
    @Column(name = "external_ports")
    Map<String, Integer> externalPorts = new HashMap<>();

    @ElementCollection
    @Column(name = "internal_ports")
    Map<String, Integer> internalPorts = new HashMap<>();

    private InstanceNetworkSetting() {}


}
