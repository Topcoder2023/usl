package com.manda.usl.kernel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导入注解
 * 批量导入指定包
 *
 * @author hongda.li
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Import {
    /**
     * 待导入的包
     *
     * @return 包所在的类
     */
    Class<?>[] value();
}
