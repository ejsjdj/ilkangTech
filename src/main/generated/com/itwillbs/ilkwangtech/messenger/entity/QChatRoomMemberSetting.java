package com.itwillbs.ilkwangtech.messenger.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoomMemberSetting is a Querydsl query type for ChatRoomMemberSetting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoomMemberSetting extends EntityPathBase<ChatRoomMemberSetting> {

    private static final long serialVersionUID = -1979617261L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoomMemberSetting chatRoomMemberSetting = new QChatRoomMemberSetting("chatRoomMemberSetting");

    public final StringPath displayRoomName = createString("displayRoomName");

    public final DateTimePath<java.time.LocalDateTime> favoritedAt = createDateTime("favoritedAt", java.time.LocalDateTime.class);

    public final QChatRoomMemberSettingId id;

    public final StringPath isFavorite = createString("isFavorite");

    public final com.itwillbs.ilkwangtech.member.entity.QMember member;

    public final QChatRoom room;

    public QChatRoomMemberSetting(String variable) {
        this(ChatRoomMemberSetting.class, forVariable(variable), INITS);
    }

    public QChatRoomMemberSetting(Path<? extends ChatRoomMemberSetting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoomMemberSetting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoomMemberSetting(PathMetadata metadata, PathInits inits) {
        this(ChatRoomMemberSetting.class, metadata, inits);
    }

    public QChatRoomMemberSetting(Class<? extends ChatRoomMemberSetting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QChatRoomMemberSettingId(forProperty("id")) : null;
        this.member = inits.isInitialized("member") ? new com.itwillbs.ilkwangtech.member.entity.QMember(forProperty("member")) : null;
        this.room = inits.isInitialized("room") ? new QChatRoom(forProperty("room")) : null;
    }

}

