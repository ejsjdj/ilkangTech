package com.itwillbs.ilkwangtech.standard.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMachineEntity is a Querydsl query type for MachineEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMachineEntity extends EntityPathBase<MachineEntity> {

    private static final long serialVersionUID = 316306544L;

    public static final QMachineEntity machineEntity = new QMachineEntity("machineEntity");

    public final StringPath electricPower = createString("electricPower");

    public final StringPath id = createString("id");

    public final StringPath manufacturer = createString("manufacturer");

    public final StringPath name = createString("name");

    public QMachineEntity(String variable) {
        super(MachineEntity.class, forVariable(variable));
    }

    public QMachineEntity(Path<? extends MachineEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMachineEntity(PathMetadata metadata) {
        super(MachineEntity.class, metadata);
    }

}

