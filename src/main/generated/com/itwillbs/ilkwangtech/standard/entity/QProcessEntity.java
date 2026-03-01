package com.itwillbs.ilkwangtech.standard.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProcessEntity is a Querydsl query type for ProcessEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProcessEntity extends EntityPathBase<ProcessEntity> {

    private static final long serialVersionUID = 828641432L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProcessEntity processEntity = new QProcessEntity("processEntity");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final StringPath name = createString("name");

    public final StringPath operationId = createString("operationId");

    public QProcessEntity(String variable) {
        this(ProcessEntity.class, forVariable(variable), INITS);
    }

    public QProcessEntity(Path<? extends ProcessEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProcessEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProcessEntity(PathMetadata metadata, PathInits inits) {
        this(ProcessEntity.class, metadata, inits);
    }

    public QProcessEntity(Class<? extends ProcessEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
    }

}

