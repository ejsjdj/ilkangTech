package com.itwillbs.ilkwangtech.production.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductionInstructEntity is a Querydsl query type for ProductionInstructEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductionInstructEntity extends EntityPathBase<ProductionInstructEntity> {

    private static final long serialVersionUID = -1180627028L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductionInstructEntity productionInstructEntity = new QProductionInstructEntity("productionInstructEntity");

    public final NumberPath<Long> defective = createNumber("defective", Long.class);

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath instructCode = createString("instructCode");

    public final NumberPath<Long> instructQty = createNumber("instructQty", Long.class);

    public final NumberPath<Long> item = createNumber("item", Long.class);

    public final NumberPath<Long> lotId = createNumber("lotId", Long.class);

    public final com.itwillbs.ilkwangtech.standard.entity.QProcessEntity process;

    public final QProductionPlaneEntity productionId;

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    public final ListPath<ProductionWorkerEntity, QProductionWorkerEntity> workers = this.<ProductionWorkerEntity, QProductionWorkerEntity>createList("workers", ProductionWorkerEntity.class, QProductionWorkerEntity.class, PathInits.DIRECT2);

    public QProductionInstructEntity(String variable) {
        this(ProductionInstructEntity.class, forVariable(variable), INITS);
    }

    public QProductionInstructEntity(Path<? extends ProductionInstructEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductionInstructEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductionInstructEntity(PathMetadata metadata, PathInits inits) {
        this(ProductionInstructEntity.class, metadata, inits);
    }

    public QProductionInstructEntity(Class<? extends ProductionInstructEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.process = inits.isInitialized("process") ? new com.itwillbs.ilkwangtech.standard.entity.QProcessEntity(forProperty("process"), inits.get("process")) : null;
        this.productionId = inits.isInitialized("productionId") ? new QProductionPlaneEntity(forProperty("productionId"), inits.get("productionId")) : null;
    }

}

