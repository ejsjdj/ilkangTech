package com.itwillbs.ilkwangtech.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFileMeta is a Querydsl query type for FileMeta
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileMeta extends EntityPathBase<FileMeta> {

    private static final long serialVersionUID = -1041225815L;

    public static final QFileMeta fileMeta = new QFileMeta("fileMeta");

    public final StringPath contentType = createString("contentType");

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath originalName = createString("originalName");

    public final StringPath storedName = createString("storedName");

    public final DateTimePath<java.time.LocalDateTime> uploadedAt = createDateTime("uploadedAt", java.time.LocalDateTime.class);

    public QFileMeta(String variable) {
        super(FileMeta.class, forVariable(variable));
    }

    public QFileMeta(Path<? extends FileMeta> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileMeta(PathMetadata metadata) {
        super(FileMeta.class, metadata);
    }

}

