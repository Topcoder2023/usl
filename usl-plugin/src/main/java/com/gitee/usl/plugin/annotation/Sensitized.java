package com.gitee.usl.plugin.annotation;

import com.gitee.usl.plugin.impl.sensitive.SensitiveType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitized {
    /**
     * 脱敏类型
     *
     * @return 脱敏类型
     */
    SensitiveType value();
}
