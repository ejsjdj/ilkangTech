package com.itwillbs.ilkwangtech.production.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductionWorkerEntity is a Querydsl query type for ProductionWorkerEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductionWorkerEntity extends EntityPathBase<ProductionWorkerEntity> {

    private static final long serialVersionUID = -574298448L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductionWorkerEntity productionWorkerEntity = new QProductionWorkerEntity("productionWorkerEntity");

    public final QProductionInstructEntity header;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lot = createString("lot");

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final com.itwillbs.ilkwangtech.standard.entity.QProcessEntity process;

    public final StringPath status = createString("status");

    public QProductionWorkerEntity(String variable) {
        this(ProductionWorkerEntity.class, forVariable(variable), INITS);
    }

    public QProductionWorkerEntity(Path<? extends ProductionWorkerEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductionWorkerEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductionWorkerEntity(PathMetadata metadata, PathInits inits) {
        this(ProductionWorkerEntity.class, metadata, inits);
    }

    public QProductionWorkerEntity(Class<? extends ProductionWorkerEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.header = inits.isInitialized("header") ? new QProductionInstructEntity(forProperty("header"), inits.get("header")) : null;
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
        this.process = inits.isInitialized("process") ? new com.itwillbs.ilkwangtech.standard.entity.QProcessEntity(forProperty("process"), inits.get("process")) : null;
    }

}

