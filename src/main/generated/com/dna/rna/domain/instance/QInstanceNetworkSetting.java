package com.dna.rna.domain.instance;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QInstanceNetworkSetting is a Querydsl query type for InstanceNetworkSetting
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QInstanceNetworkSetting extends BeanPath<InstanceNetworkSetting> {

    private static final long serialVersionUID = -64013741L;

    public static final QInstanceNetworkSetting instanceNetworkSetting = new QInstanceNetworkSetting("instanceNetworkSetting");

    public final MapPath<String, Integer, NumberPath<Integer>> externalPorts = this.<String, Integer, NumberPath<Integer>>createMap("externalPorts", String.class, Integer.class, NumberPath.class);

    public final MapPath<String, Integer, NumberPath<Integer>> internalPorts = this.<String, Integer, NumberPath<Integer>>createMap("internalPorts", String.class, Integer.class, NumberPath.class);

    public QInstanceNetworkSetting(String variable) {
        super(InstanceNetworkSetting.class, forVariable(variable));
    }

    public QInstanceNetworkSetting(Path<? extends InstanceNetworkSetting> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInstanceNetworkSetting(PathMetadata metadata) {
        super(InstanceNetworkSetting.class, metadata);
    }

}

