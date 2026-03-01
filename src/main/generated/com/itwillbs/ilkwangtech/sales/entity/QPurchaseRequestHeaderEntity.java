package com.itwillbs.ilkwangtech.sales.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchaseRequestHeaderEntity is a Querydsl query type for PurchaseRequestHeaderEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseRequestHeaderEntity extends EntityPathBase<PurchaseRequestHeaderEntity> {

    private static final long serialVersionUID = 1757766901L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchaseRequestHeaderEntity purchaseRequestHeaderEntity = new QPurchaseRequestHeaderEntity("purchaseRequestHeaderEntity");

    public final StringPath contractType = createString("contractType");

    public final StringPath deliveryLocate = createString("deliveryLocate");

    public final DatePath<java.time.LocalDate> dueDate = createDate("dueDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<PurchaseRequestEntity, QPurchaseRequestEntity> lines = this.<PurchaseRequestEntity, QPurchaseRequestEntity>createList("lines", PurchaseRequestEntity.class, QPurchaseRequestEntity.class, PathInits.DIRECT2);

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final StringPath produceType = createString("produceType");

    public final StringPath purchaseRequestCode = createString("purchaseRequestCode");

    public final DatePath<java.time.LocalDate> requestDate = createDate("requestDate", java.time.LocalDate.class);

    public QPurchaseRequestHeaderEntity(String variable) {
        this(PurchaseRequestHeaderEntity.class, forVariable(variable), INITS);
    }

    public QPurchaseRequestHeaderEntity(Path<? extends PurchaseRequestHeaderEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPurchaseRequestHeaderEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPurchaseRequestHeaderEntity(PathMetadata metadata, PathInits inits) {
        this(PurchaseRequestHeaderEntity.class, metadata, inits);
    }

    public QPurchaseRequestHeaderEntity(Class<? extends PurchaseRequestHeaderEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
    }

}

