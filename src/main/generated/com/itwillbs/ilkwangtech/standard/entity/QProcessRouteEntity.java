package com.itwillbs.ilkwangtech.standard.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProcessRouteEntity is a Querydsl query type for ProcessRouteEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProcessRouteEntity extends EntityPathBase<ProcessRouteEntity> {

    private static final long serialVersionUID = -2132188169L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProcessRouteEntity processRouteEntity = new QProcessRouteEntity("processRouteEntity");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItemEntity item;

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final StringPath note = createString("note");

    public final QProcessEntity operation;

    public final StringPath routeCode = createString("routeCode");

    public final StringPath routeName = createString("routeName");

    public final NumberPath<Long> sequence = createNumber("sequence", Long.class);

    public QProcessRouteEntity(String variable) {
        this(ProcessRouteEntity.class, forVariable(variable), INITS);
    }

    public QProcessRouteEntity(Path<? extends ProcessRouteEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProcessRouteEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProcessRouteEntity(PathMetadata metadata, PathInits inits) {
        this(ProcessRouteEntity.class, metadata, inits);
    }

    public QProcessRouteEntity(Class<? extends ProcessRouteEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItemEntity(forProperty("item")) : null;
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
        this.operation = inits.isInitialized("operation") ? new QProcessEntity(forProperty("operation"), inits.get("operation")) : null;
    }

}

