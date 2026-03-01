package com.itwillbs.ilkwangtech.equipment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEqFailure is a Querydsl query type for EqFailure
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEqFailure extends EntityPathBase<EqFailure> {

    private static final long serialVersionUID = 1092958579L;

    public static final QEqFailure eqFailure = new QEqFailure("eqFailure");

    public final StringPath equipCode = createString("equipCode");

    public final StringPath failureDesc = createString("failureDesc");

    public final NumberPath<Long> failureId = createNumber("failureId", Long.class);

    public final StringPath failureLevel = createString("failureLevel");

    public final DateTimePath<java.time.LocalDateTime> fixedAt = createDateTime("fixedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> occurredAt = createDateTime("occurredAt", java.time.LocalDateTime.class);

    public final StringPath procCode = createString("procCode");

    public final StringPath repairStaff = createString("repairStaff");

    public final StringPath status = createString("status");

    public QEqFailure(String variable) {
        super(EqFailure.class, forVariable(variable));
    }

    public QEqFailure(Path<? extends EqFailure> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEqFailure(PathMetadata metadata) {
        super(EqFailure.class, metadata);
    }

}

