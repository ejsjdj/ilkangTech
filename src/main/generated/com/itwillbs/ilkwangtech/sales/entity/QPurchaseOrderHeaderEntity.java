package com.itwillbs.ilkwangtech.sales.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchaseOrderHeaderEntity is a Querydsl query type for PurchaseOrderHeaderEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseOrderHeaderEntity extends EntityPathBase<PurchaseOrderHeaderEntity> {

    private static final long serialVersionUID = 1260476596L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchaseOrderHeaderEntity purchaseOrderHeaderEntity = new QPurchaseOrderHeaderEntity("purchaseOrderHeaderEntity");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final StringPath company = createString("company");

    public final StringPath companyManager = createString("companyManager");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final DatePath<java.time.LocalDate> orderDate = createDate("orderDate", java.time.LocalDate.class);

    public final StringPath phone = createString("phone");

    public final StringPath purchaseOrderCode = createString("purchaseOrderCode");

    public final StringPath status = createString("status");

    public QPurchaseOrderHeaderEntity(String variable) {
        this(PurchaseOrderHeaderEntity.class, forVariable(variable), INITS);
    }

    public QPurchaseOrderHeaderEntity(Path<? extends PurchaseOrderHeaderEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPurchaseOrderHeaderEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPurchaseOrderHeaderEntity(PathMetadata metadata, PathInits inits) {
        this(PurchaseOrderHeaderEntity.class, metadata, inits);
    }

    public QPurchaseOrderHeaderEntity(Class<? extends PurchaseOrderHeaderEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
    }

}

