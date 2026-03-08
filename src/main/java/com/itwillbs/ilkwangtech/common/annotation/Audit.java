package com.itwillbs.ilkwangtech.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
    String action(); // 어떤 작업인지 (예: "수주 등록")
    String entity() default ""; // 대상 엔티티 명
}
