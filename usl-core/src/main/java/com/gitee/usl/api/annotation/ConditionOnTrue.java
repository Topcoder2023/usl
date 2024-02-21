package com.gitee.usl.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当配置中存在指定键，且指定键为 true 时才会生效
 *
 * @author hongda.li
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionOnTrue {
    /**
     * 唯一键的名称
     *
     * @return 唯一键的名称
     */
    String value();
}
