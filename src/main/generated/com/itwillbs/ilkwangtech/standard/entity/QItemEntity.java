package com.itwillbs.ilkwangtech.standard.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QItemEntity is a Querydsl query type for ItemEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemEntity extends EntityPathBase<ItemEntity> {

    private static final long serialVersionUID = 760834352L;

    public static final QItemEntity itemEntity = new QItemEntity("itemEntity");

    public final StringPath itemCode = createString("itemCode");

    public final NumberPath<Long> itemId = createNumber("itemId", Long.class);

    public final StringPath itemName = createString("itemName");

    public final EnumPath<com.itwillbs.ilkwangtech.item.constant.ItemType> itemType = createEnum("itemType", com.itwillbs.ilkwangtech.item.constant.ItemType.class);

    public final NumberPath<Long> standardPrice = createNumber("standardPrice", Long.class);

    public final StringPath uom = createString("uom");

    public QItemEntity(String variable) {
        super(ItemEntity.class, forVariable(variable));
    }

    public QItemEntity(Path<? extends ItemEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemEntity(PathMetadata metadata) {
        super(ItemEntity.class, metadata);
    }

}

