package com.itwillbs.ilkwangtech.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1808780015L;

    public static final QMember member = new QMember("member1");

    public final StringPath accountNumber = createString("accountNumber");

    public final StringPath accountPictureLink = createString("accountPictureLink");

    public final NumberPath<Integer> bank = createNumber("bank", Integer.class);

    public final NumberPath<Integer> department = createNumber("department", Integer.class);

    public final StringPath email = createString("email");

    public final StringPath employeeNumber = createString("employeeNumber");

    public final NumberPath<Integer> gender = createNumber("gender", Integer.class);

    public final DatePath<java.time.LocalDate> hireDate = createDate("hireDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastLogin = createDateTime("lastLogin", java.time.LocalDateTime.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Integer> position = createNumber("position", Integer.class);

    public final ListPath<com.itwillbs.ilkwangtech.account.entity.ProfileImg, com.itwillbs.ilkwangtech.account.entity.QProfileImg> profileImgs = this.<com.itwillbs.ilkwangtech.account.entity.ProfileImg, com.itwillbs.ilkwangtech.account.entity.QProfileImg>createList("profileImgs", com.itwillbs.ilkwangtech.account.entity.ProfileImg.class, com.itwillbs.ilkwangtech.account.entity.QProfileImg.class, PathInits.DIRECT2);

    public final StringPath residentNumber = createString("residentNumber");

    public final ListPath<MemberRole, QMemberRole> roles = this.<MemberRole, QMemberRole>createList("roles", MemberRole.class, QMemberRole.class, PathInits.DIRECT2);

    public final EnumPath<com.itwillbs.ilkwangtech.account.constant.MemberStatus> status = createEnum("status", com.itwillbs.ilkwangtech.account.constant.MemberStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

