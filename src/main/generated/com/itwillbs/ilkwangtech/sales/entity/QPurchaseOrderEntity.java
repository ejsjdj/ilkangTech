package com.itwillbs.ilkwangtech.sales.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchaseOrderEntity is a Querydsl query type for PurchaseOrderEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseOrderEntity extends EntityPathBase<PurchaseOrderEntity> {

    private static final long serialVersionUID = -2088492345L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchaseOrderEntity purchaseOrderEntity = new QPurchaseOrderEntity("purchaseOrderEntity");

    public final QPurchaseOrderHeaderEntity header;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.itwillbs.ilkwangtech.standard.entity.QItemEntity item;

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final NumberPath<Long> unitPrice = createNumber("unitPrice", Long.class);

    public QPurchaseOrderEntity(String variable) {
        this(PurchaseOrderEntity.class, forVariable(variable), INITS);
    }

    public QPurchaseOrderEntity(Path<? extends PurchaseOrderEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPurchaseOrderEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPurchaseOrderEntity(PathMetadata metadata, PathInits inits) {
        this(PurchaseOrderEntity.class, metadata, inits);
    }

    public QPurchaseOrderEntity(Class<? extends PurchaseOrderEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.header = inits.isInitialized("header") ? new QPurchaseOrderHeaderEntity(forProperty("header"), inits.get("header")) : null;
        this.item = inits.isInitialized("item") ? new com.itwillbs.ilkwangtech.standard.entity.QItemEntity(forProperty("item")) : null;
    }

}

