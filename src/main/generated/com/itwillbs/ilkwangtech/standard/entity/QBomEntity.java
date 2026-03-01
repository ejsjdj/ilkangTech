package com.itwillbs.ilkwangtech.standard.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBomEntity is a Querydsl query type for BomEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBomEntity extends EntityPathBase<BomEntity> {

    private static final long serialVersionUID = -1543859671L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBomEntity bomEntity = new QBomEntity("bomEntity");

    public final QItemEntity afterItem;

    public final QItemEntity beforeItem;

    public final NumberPath<Long> bomId = createNumber("bomId", Long.class);

    public final NumberPath<Long> requireQty = createNumber("requireQty", Long.class);

    public QBomEntity(String variable) {
        this(BomEntity.class, forVariable(variable), INITS);
    }

    public QBomEntity(Path<? extends BomEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBomEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBomEntity(PathMetadata metadata, PathInits inits) {
        this(BomEntity.class, metadata, inits);
    }

    public QBomEntity(Class<? extends BomEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.afterItem = inits.isInitialized("afterItem") ? new QItemEntity(forProperty("afterItem")) : null;
        this.beforeItem = inits.isInitialized("beforeItem") ? new QItemEntity(forProperty("beforeItem")) : null;
    }

}

