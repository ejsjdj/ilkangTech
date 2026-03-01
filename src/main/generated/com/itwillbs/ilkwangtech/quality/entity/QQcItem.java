package com.itwillbs.ilkwangtech.quality.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQcItem is a Querydsl query type for QcItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQcItem extends EntityPathBase<QcItem> {

    private static final long serialVersionUID = -815172639L;

    public static final QQcItem qcItem = new QQcItem("qcItem");

    public final StringPath content = createString("content");

    public final StringPath procCode = createString("procCode");

    public final NumberPath<Long> qcItemId = createNumber("qcItemId", Long.class);

    public final StringPath qcName = createString("qcName");

    public QQcItem(String variable) {
        super(QcItem.class, forVariable(variable));
    }

    public QQcItem(Path<? extends QcItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQcItem(PathMetadata metadata) {
        super(QcItem.class, metadata);
    }

}

