package com.itwillbs.ilkwangtech.equipment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEqHistory is a Querydsl query type for EqHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEqHistory extends EntityPathBase<EqHistory> {

    private static final long serialVersionUID = -1188500355L;

    public static final QEqHistory eqHistory = new QEqHistory("eqHistory");

    public final StringPath duration = createString("duration");

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> eqHistoryId = createNumber("eqHistoryId", Long.class);

    public final StringPath eqName = createString("eqName");

    public final StringPath operatorName = createString("operatorName");

    public final StringPath productName = createString("productName");

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    public final StringPath workOrderNo = createString("workOrderNo");

    public QEqHistory(String variable) {
        super(EqHistory.class, forVariable(variable));
    }

    public QEqHistory(Path<? extends EqHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEqHistory(PathMetadata metadata) {
        super(EqHistory.class, metadata);
    }

}

