package com.dna.rna.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRolePermission is a Querydsl query type for RolePermission
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRolePermission extends EntityPathBase<RolePermission> {

    private static final long serialVersionUID = -2071455605L;

    public static final QRolePermission rolePermission = new QRolePermission("rolePermission");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QRolePermission(String variable) {
        super(RolePermission.class, forVariable(variable));
    }

    public QRolePermission(Path<? extends RolePermission> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRolePermission(PathMetadata metadata) {
        super(RolePermission.class, metadata);
    }

}

