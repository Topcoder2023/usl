package com.gitee.usl.api.annotation;

import cn.hutool.core.text.CharSequenceUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionGroup {
    String value() default CharSequenceUtil.EMPTY;
}
