package com.gitee.usl.plugin.annotation;

import com.gitee.usl.infra.constant.StringConstant;

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
    String value() default StringConstant.FIRST_USL_RUNNER_NAME;

    /**
     * 指定异步线程池所使用的名称
     * 如果指定了此项配置，则 value() 属性失效
     * 线程池需要注入 Singleton.put(name, value) 中
     *
     * @return 线程池名称
     */
    String beanName() default "";
}
