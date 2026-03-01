package com.itwillbs.ilkwangtech.messenger.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatReadState is a Querydsl query type for ChatReadState
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatReadState extends EntityPathBase<ChatReadState> {

    private static final long serialVersionUID = -808531085L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatReadState chatReadState = new QChatReadState("chatReadState");

    public final QChatReadStateId id;

    public final QChatMessage lastReadMessage;

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final QChatRoom room;

    public QChatReadState(String variable) {
        this(ChatReadState.class, forVariable(variable), INITS);
    }

    public QChatReadState(Path<? extends ChatReadState> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatReadState(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatReadState(PathMetadata metadata, PathInits inits) {
        this(ChatReadState.class, metadata, inits);
    }

    public QChatReadState(Class<? extends ChatReadState> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QChatReadStateId(forProperty("id")) : null;
        this.lastReadMessage = inits.isInitialized("lastReadMessage") ? new QChatMessage(forProperty("lastReadMessage")) : null;
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
        this.room = inits.isInitialized("room") ? new QChatRoom(forProperty("room")) : null;
    }

}

