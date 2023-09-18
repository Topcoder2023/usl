package com.gitee.usl.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UslParam {
    /**
     * 从函数调用上下文参数中，根据参数的名称取出指定参数
     *
     * @return 返回参数的名称
     */
    String value();
}
