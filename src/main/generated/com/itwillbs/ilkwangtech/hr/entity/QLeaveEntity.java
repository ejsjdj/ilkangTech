package com.itwillbs.ilkwangtech.hr.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLeaveEntity is a Querydsl query type for LeaveEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLeaveEntity extends EntityPathBase<LeaveEntity> {

    private static final long serialVersionUID = -1202116365L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLeaveEntity leaveEntity = new QLeaveEntity("leaveEntity");

    public final NumberPath<Long> leaveId = createNumber("leaveId", Long.class);

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final NumberPath<Long> remainLeave = createNumber("remainLeave", Long.class);

    public final NumberPath<Long> totalLeave = createNumber("totalLeave", Long.class);

    public final NumberPath<Long> usedLeave = createNumber("usedLeave", Long.class);

    public QLeaveEntity(String variable) {
        this(LeaveEntity.class, forVariable(variable), INITS);
    }

    public QLeaveEntity(Path<? extends LeaveEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLeaveEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLeaveEntity(PathMetadata metadata, PathInits inits) {
        this(LeaveEntity.class, metadata, inits);
    }

    public QLeaveEntity(Class<? extends LeaveEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
    }

}

