package com.itwillbs.ilkwangtech.equipment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEquipment is a Querydsl query type for Equipment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEquipment extends EntityPathBase<Equipment> {

    private static final long serialVersionUID = 91470787L;

    public static final QEquipment equipment = new QEquipment("equipment");

    public final StringPath equipCode = createString("equipCode");

    public final StringPath equipName = createString("equipName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> instDate = createDate("instDate", java.time.LocalDate.class);

    public final StringPath maker = createString("maker");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final StringPath procCode = createString("procCode");

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final StringPath useYn = createString("useYn");

    public QEquipment(String variable) {
        super(Equipment.class, forVariable(variable));
    }

    public QEquipment(Path<? extends Equipment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEquipment(PathMetadata metadata) {
        super(Equipment.class, metadata);
    }

}

