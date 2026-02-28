package com.itwillbs.ilkwangtech.production.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductionPlaneEntity is a Querydsl query type for ProductionPlaneEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductionPlaneEntity extends EntityPathBase<ProductionPlaneEntity> {

    private static final long serialVersionUID = 1995385296L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductionPlaneEntity productionPlaneEntity = new QProductionPlaneEntity("productionPlaneEntity");

    public final ListPath<ProductionPlaneDetailEntity, QProductionPlaneDetailEntity> details = this.<ProductionPlaneDetailEntity, QProductionPlaneDetailEntity>createList("details", ProductionPlaneDetailEntity.class, QProductionPlaneDetailEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> item = createNumber("item", Long.class);

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final StringPath memo = createString("memo");

    public final StringPath planeCode = createString("planeCode");

    public final DateTimePath<java.time.LocalDateTime> planeDate = createDateTime("planeDate", java.time.LocalDateTime.class);

    public final com.itwillbs.ilkwangtech.standard.entity.QProcessRouteEntity route;

    public final StringPath status = createString("status");

    public final NumberPath<Long> totalQty = createNumber("totalQty", Long.class);

    public QProductionPlaneEntity(String variable) {
        this(ProductionPlaneEntity.class, forVariable(variable), INITS);
    }

    public QProductionPlaneEntity(Path<? extends ProductionPlaneEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductionPlaneEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductionPlaneEntity(PathMetadata metadata, PathInits inits) {
        this(ProductionPlaneEntity.class, metadata, inits);
    }

    public QProductionPlaneEntity(Class<? extends ProductionPlaneEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
        this.route = inits.isInitialized("route") ? new com.itwillbs.ilkwangtech.standard.entity.QProcessRouteEntity(forProperty("route"), inits.get("route")) : null;
    }

}

