package com.dna.rna.domain.instanceLog;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QInstanceLog is a Querydsl query type for InstanceLog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInstanceLog extends EntityPathBase<InstanceLog> {

    private static final long serialVersionUID = 1042261203L;

    public static final QInstanceLog instanceLog = new QInstanceLog("instanceLog");

    public final NumberPath<Long> instanceLogId = createNumber("instanceLogId", Long.class);

    public final StringPath logFileName = createString("logFileName");

    public final NumberPath<Integer> logType = createNumber("logType", Integer.class);

    public QInstanceLog(String variable) {
        super(InstanceLog.class, forVariable(variable));
    }

    public QInstanceLog(Path<? extends InstanceLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInstanceLog(PathMetadata metadata) {
        super(InstanceLog.class, metadata);
    }

}

