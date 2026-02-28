package com.itwillbs.ilkwangtech.sales.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchaseRequestEntity is a Querydsl query type for PurchaseRequestEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseRequestEntity extends EntityPathBase<PurchaseRequestEntity> {

    private static final long serialVersionUID = -13454392L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchaseRequestEntity purchaseRequestEntity = new QPurchaseRequestEntity("purchaseRequestEntity");

    public final QPurchaseRequestHeaderEntity header;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.itwillbs.ilkwangtech.standard.entity.QItemEntity item;

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public QPurchaseRequestEntity(String variable) {
        this(PurchaseRequestEntity.class, forVariable(variable), INITS);
    }

    public QPurchaseRequestEntity(Path<? extends PurchaseRequestEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPurchaseRequestEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPurchaseRequestEntity(PathMetadata metadata, PathInits inits) {
        this(PurchaseRequestEntity.class, metadata, inits);
    }

    public QPurchaseRequestEntity(Class<? extends PurchaseRequestEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.header = inits.isInitialized("header") ? new QPurchaseRequestHeaderEntity(forProperty("header"), inits.get("header")) : null;
        this.item = inits.isInitialized("item") ? new com.itwillbs.ilkwangtech.standard.entity.QItemEntity(forProperty("item")) : null;
    }

}

