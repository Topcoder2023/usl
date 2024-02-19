package com.gitee.usl.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数字格式验证注解
 * @author jingshu.zeng
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Digits {
    /**
     * 整数位数上限
     */
    int integer();

    /**
     * 小数位数上限
     */
    int fraction();

    String message() default "函数 [{name}] 的第 [{index}] 个参数值为 [{value}]，不是合法的数字格式";
}
