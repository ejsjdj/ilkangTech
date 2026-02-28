package com.itwillbs.ilkwangtech.hr.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttendance is a Querydsl query type for Attendance
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttendance extends EntityPathBase<Attendance> {

    private static final long serialVersionUID = -1135321008L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttendance attendance = new QAttendance("attendance");

    public final NumberPath<Long> approverId = createNumber("approverId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> goOutTime = createDateTime("goOutTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> inTime = createDateTime("inTime", java.time.LocalDateTime.class);

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final StringPath memo = createString("memo");

    public final DateTimePath<java.time.LocalDateTime> outTime = createDateTime("outTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> returnTime = createDateTime("returnTime", java.time.LocalDateTime.class);

    public final EnumPath<com.itwillbs.ilkwangtech.hr.constant.AttendanceStatus> status = createEnum("status", com.itwillbs.ilkwangtech.hr.constant.AttendanceStatus.class);

    public final DatePath<java.time.LocalDate> workDate = createDate("workDate", java.time.LocalDate.class);

    public QAttendance(String variable) {
        this(Attendance.class, forVariable(variable), INITS);
    }

    public QAttendance(Path<? extends Attendance> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttendance(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttendance(PathMetadata metadata, PathInits inits) {
        this(Attendance.class, metadata, inits);
    }

    public QAttendance(Class<? extends Attendance> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
    }

}

