package com.itwillbs.ilkwangtech.messenger.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatRoomMemberSettingId is a Querydsl query type for ChatRoomMemberSettingId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QChatRoomMemberSettingId extends BeanPath<ChatRoomMemberSettingId> {

    private static final long serialVersionUID = 258326670L;

    public static final QChatRoomMemberSettingId chatRoomMemberSettingId = new QChatRoomMemberSettingId("chatRoomMemberSettingId");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> roomId = createNumber("roomId", Long.class);

    public QChatRoomMemberSettingId(String variable) {
        super(ChatRoomMemberSettingId.class, forVariable(variable));
    }

    public QChatRoomMemberSettingId(Path<? extends ChatRoomMemberSettingId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatRoomMemberSettingId(PathMetadata metadata) {
        super(ChatRoomMemberSettingId.class, metadata);
    }

}

