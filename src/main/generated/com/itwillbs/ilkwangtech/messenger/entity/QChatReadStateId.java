package com.itwillbs.ilkwangtech.messenger.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatReadStateId is a Querydsl query type for ChatReadStateId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QChatReadStateId extends BeanPath<ChatReadStateId> {

    private static final long serialVersionUID = 390710254L;

    public static final QChatReadStateId chatReadStateId = new QChatReadStateId("chatReadStateId");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> roomId = createNumber("roomId", Long.class);

    public QChatReadStateId(String variable) {
        super(ChatReadStateId.class, forVariable(variable));
    }

    public QChatReadStateId(Path<? extends ChatReadStateId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatReadStateId(PathMetadata metadata) {
        super(ChatReadStateId.class, metadata);
    }

}

