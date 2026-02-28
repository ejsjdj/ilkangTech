package com.itwillbs.ilkwangtech.production.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductionPlaneDetailEntity is a Querydsl query type for ProductionPlaneDetailEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductionPlaneDetailEntity extends EntityPathBase<ProductionPlaneDetailEntity> {

    private static final long serialVersionUID = -1845360703L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductionPlaneDetailEntity productionPlaneDetailEntity = new QProductionPlaneDetailEntity("productionPlaneDetailEntity");

    public final QProductionPlaneEntity header;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final NumberPath<Long> productQty = createNumber("productQty", Long.class);

    public QProductionPlaneDetailEntity(String variable) {
        this(ProductionPlaneDetailEntity.class, forVariable(variable), INITS);
    }

    public QProductionPlaneDetailEntity(Path<? extends ProductionPlaneDetailEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductionPlaneDetailEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductionPlaneDetailEntity(PathMetadata metadata, PathInits inits) {
        this(ProductionPlaneDetailEntity.class, metadata, inits);
    }

    public QProductionPlaneDetailEntity(Class<? extends ProductionPlaneDetailEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.header = inits.isInitialized("header") ? new QProductionPlaneEntity(forProperty("header"), inits.get("header")) : null;
    }

}

