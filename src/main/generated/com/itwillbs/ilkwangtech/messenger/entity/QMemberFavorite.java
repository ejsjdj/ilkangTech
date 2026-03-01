package com.itwillbs.ilkwangtech.messenger.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberFavorite is a Querydsl query type for MemberFavorite
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberFavorite extends EntityPathBase<MemberFavorite> {

    private static final long serialVersionUID = 1717564678L;

    public static final QMemberFavorite memberFavorite = new QMemberFavorite("memberFavorite");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> targetId = createNumber("targetId", Long.class);

    public QMemberFavorite(String variable) {
        super(MemberFavorite.class, forVariable(variable));
    }

    public QMemberFavorite(Path<? extends MemberFavorite> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberFavorite(PathMetadata metadata) {
        super(MemberFavorite.class, metadata);
    }

}

