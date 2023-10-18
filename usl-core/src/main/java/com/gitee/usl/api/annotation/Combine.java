package com.gitee.usl.api.annotation;

import com.gitee.usl.infra.constant.StringConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Combine {
    /**
     * 脚本内容
     *
     * @return 脚本内容
     */
    String value();

    /**
     * 执行器的名称
     *
     * @return 执行器名称
     */
    String runnerName() default StringConstant.FIRST_USL_RUNNER_NAME;
}
