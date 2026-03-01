package com.itwillbs.ilkwangtech.account.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLoginAttempt is a Querydsl query type for LoginAttempt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoginAttempt extends EntityPathBase<LoginAttempt> {

    private static final long serialVersionUID = -1817635858L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLoginAttempt loginAttempt = new QLoginAttempt("loginAttempt");

    public final BooleanPath accountNonLocked = createBoolean("accountNonLocked");

    public final NumberPath<Integer> failedCount = createNumber("failedCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lockTime = createDateTime("lockTime", java.time.LocalDateTime.class);

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public QLoginAttempt(String variable) {
        this(LoginAttempt.class, forVariable(variable), INITS);
    }

    public QLoginAttempt(Path<? extends LoginAttempt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLoginAttempt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLoginAttempt(PathMetadata metadata, PathInits inits) {
        this(LoginAttempt.class, metadata, inits);
    }

    public QLoginAttempt(Class<? extends LoginAttempt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
    }

}

