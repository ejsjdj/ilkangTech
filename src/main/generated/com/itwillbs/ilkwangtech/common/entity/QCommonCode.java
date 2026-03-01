package com.itwillbs.ilkwangtech.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommonCode is a Querydsl query type for CommonCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommonCode extends EntityPathBase<CommonCode> {

    private static final long serialVersionUID = -322715008L;

    public static final QCommonCode commonCode = new QCommonCode("commonCode");

    public final StringPath CommonCode = createString("CommonCode");

    public final StringPath CommonCodeName = createString("CommonCodeName");

    public final StringPath description = createString("description");

    public final StringPath groupCode = createString("groupCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath useYn = createString("useYn");

    public QCommonCode(String variable) {
        super(CommonCode.class, forVariable(variable));
    }

    public QCommonCode(Path<? extends CommonCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommonCode(PathMetadata metadata) {
        super(CommonCode.class, metadata);
    }

}

