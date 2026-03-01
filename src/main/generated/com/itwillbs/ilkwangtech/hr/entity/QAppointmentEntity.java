package com.itwillbs.ilkwangtech.hr.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAppointmentEntity is a Querydsl query type for AppointmentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAppointmentEntity extends EntityPathBase<AppointmentEntity> {

    private static final long serialVersionUID = 1244440859L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAppointmentEntity appointmentEntity = new QAppointmentEntity("appointmentEntity");

    public final NumberPath<Long> appointmentId = createNumber("appointmentId", Long.class);

    public final DatePath<java.time.LocalDate> approveDate = createDate("approveDate", java.time.LocalDate.class);

    public final StringPath approveStatus = createString("approveStatus");

    public final NumberPath<Integer> currentDept = createNumber("currentDept", Integer.class);

    public final NumberPath<Integer> currentRank = createNumber("currentRank", Integer.class);

    public final QDraftEntity draft;

    public final com.itwillbs.ilkwangtech.member.entity.QMember memberId;

    public final NumberPath<Integer> preDept = createNumber("preDept", Integer.class);

    public final NumberPath<Integer> preRank = createNumber("preRank", Integer.class);

    public final StringPath workStatus = createString("workStatus");

    public QAppointmentEntity(String variable) {
        this(AppointmentEntity.class, forVariable(variable), INITS);
    }

    public QAppointmentEntity(Path<? extends AppointmentEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAppointmentEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAppointmentEntity(PathMetadata metadata, PathInits inits) {
        this(AppointmentEntity.class, metadata, inits);
    }

    public QAppointmentEntity(Class<? extends AppointmentEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.draft = inits.isInitialized("draft") ? new QDraftEntity(forProperty("draft"), inits.get("draft")) : null;
        this.memberId = inits.isInitialized("memberId") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("memberId")) : null;
    }

}

