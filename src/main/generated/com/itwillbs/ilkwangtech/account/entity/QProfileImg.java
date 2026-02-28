package com.itwillbs.ilkwangtech.account.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProfileImg is a Querydsl query type for ProfileImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfileImg extends EntityPathBase<ProfileImg> {

    private static final long serialVersionUID = -281181020L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProfileImg profileImg = new QProfileImg("profileImg");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgLocation = createString("imgLocation");

    public final StringPath imgName = createString("imgName");

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final StringPath originalImgName = createString("originalImgName");

    public final StringPath repImgYn = createString("repImgYn");

    public QProfileImg(String variable) {
        this(ProfileImg.class, forVariable(variable), INITS);
    }

    public QProfileImg(Path<? extends ProfileImg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProfileImg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProfileImg(PathMetadata metadata, PathInits inits) {
        this(ProfileImg.class, metadata, inits);
    }

    public QProfileImg(Class<? extends ProfileImg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
    }

}

