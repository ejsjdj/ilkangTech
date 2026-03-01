package com.itwillbs.ilkwangtech.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScheduleFile is a Querydsl query type for ScheduleFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScheduleFile extends EntityPathBase<ScheduleFile> {

    private static final long serialVersionUID = 602678631L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScheduleFile scheduleFile = new QScheduleFile("scheduleFile");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final StringPath savedFileName = createString("savedFileName");

    public final QSchedule schedule;

    public QScheduleFile(String variable) {
        this(ScheduleFile.class, forVariable(variable), INITS);
    }

    public QScheduleFile(Path<? extends ScheduleFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScheduleFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScheduleFile(PathMetadata metadata, PathInits inits) {
        this(ScheduleFile.class, metadata, inits);
    }

    public QScheduleFile(Class<? extends ScheduleFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.schedule = inits.isInitialized("schedule") ? new QSchedule(forProperty("schedule"), inits.get("schedule")) : null;
    }

}

