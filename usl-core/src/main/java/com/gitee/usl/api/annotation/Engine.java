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

    @Description("使用的脚本引擎名称")
    EngineName value() default EngineName.JS;

}
