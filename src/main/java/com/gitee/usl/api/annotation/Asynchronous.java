package com.gitee.usl.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Asynchronous {
    /**
     * 指定异步线程池所使用的配置
     * 因线程池配置与执行器绑定
     * 因此仅需返回执行器的名称
     *
     * @return 执行器的名称
     */
    String value() default CombineFunc.DEFAULT_ENGINE_NAME;
}
