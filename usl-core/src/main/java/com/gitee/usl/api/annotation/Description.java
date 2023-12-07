package com.gitee.usl.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target({ElementType.METHOD,
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE,
        ElementType.LOCAL_VARIABLE,
        ElementType.TYPE_PARAMETER,
        ElementType.TYPE_USE,
        ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    String value();
}
