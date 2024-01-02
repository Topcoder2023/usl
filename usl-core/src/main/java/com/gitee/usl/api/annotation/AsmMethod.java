package com.gitee.usl.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Description("ASM方法访问声明注释")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsmMethod {
}
