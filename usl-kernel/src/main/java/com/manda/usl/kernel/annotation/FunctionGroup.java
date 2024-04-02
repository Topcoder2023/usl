package com.manda.usl.kernel.annotation;

import com.manda.usl.kernel.USLRunner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 函数组标记
 *
 * @author hongda.li
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionGroup {
    /**
     * 执行器的名称
     *
     * @return 名称
     */
    String value() default USLRunner.DEFAULT_NAME;

    /**
     * 插件列表
     *
     * @return 插件列表
     */
    Class<?>[] plugins() default {};
}
