package com.gitee.usl.api.annotation;

import com.gitee.usl.infra.enums.EngineName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Engine {
    /**
     * 使用的脚本引擎名称
     * 默认使用 JavaScript 脚本引擎
     * 该脚本引擎内置于 JDK8 中，无需额外引入依赖
     *
     * @return 引擎名称
     */
    EngineName value() default EngineName.JS;
}
