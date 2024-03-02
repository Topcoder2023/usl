package com.gitee.usl.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Import {
    /**
     * 需要导入的类集合
     *
     * @return 类集合
     */
    Class<?>[] value();
}
