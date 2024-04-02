package com.manda.usl.kernel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单例模式注解
 * 标记一个单例模式的类
 *
 * @author hongda.li
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Singleton {

    /**
     * 对象名称
     *
     * @return 默认为类名首字母小写
     */
    String value() default "";

    /**
     * 是否当且仅当不存在其它对象时才注册
     *
     * @return true / false
     */
    Class<?> whenNotExist() default Class.class;
}
