package com.itwillbs.ilkwangtech.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchedule is a Querydsl query type for Schedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchedule extends EntityPathBase<Schedule> {

    private static final long serialVersionUID = -1468114613L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchedule schedule = new QSchedule("schedule");

    public final StringPath attachmentFile = createString("attachmentFile");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final ListPath<ScheduleFile, QScheduleFile> files = this.<ScheduleFile, QScheduleFile>createList("files", ScheduleFile.class, QScheduleFile.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final SetPath<com.itwillbs.ilkwangtech.account.entity.Department, com.itwillbs.ilkwangtech.account.entity.QDepartment> sharedDepartments = this.<com.itwillbs.ilkwangtech.account.entity.Department, com.itwillbs.ilkwangtech.account.entity.QDepartment>createSet("sharedDepartments", com.itwillbs.ilkwangtech.account.entity.Department.class, com.itwillbs.ilkwangtech.account.entity.QDepartment.class, PathInits.DIRECT2);

    public final SetPath<com.itwillbs.ilkwangtech.member.entity.Member, com.itwillbs.ilkwangtech.member.entity.QMember> sharedMembers = this.<com.itwillbs.ilkwangtech.member.entity.Member, com.itwillbs.ilkwangtech.member.entity.QMember>createSet("sharedMembers", com.itwillbs.ilkwangtech.member.entity.Member.class, com.itwillbs.ilkwangtech.member.entity.QMember.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final StringPath thumbnailPath = createString("thumbnailPath");

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final com.itwillbs.ilkwangtech.member.entity.QMember writer;

    public QSchedule(String variable) {
        this(Schedule.class, forVariable(variable), INITS);
    }

    public QSchedule(Path<? extends Schedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchedule(PathMetadata metadata, PathInits inits) {
        this(Schedule.class, metadata, inits);
    }

    public QSchedule(Class<? extends Schedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writer = inits.isInitialized("writer") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("writer")) : null;
    }

}

