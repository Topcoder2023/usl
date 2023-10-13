package com.gitee.usl.api.annotation;

import com.gitee.usl.infra.constant.NumberConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 有序注解
 * 该注解仅能使用在类上
 * 被标记的类为一个有序类
 * 有序类的顺序取决于注解的值
 *
 * @author hongda.li
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    /**
     * 返回有序类的具体顺序
     * 返回的数字越小，则表示优先级越高
     * 默认顺序为 0
     * 最小值为 0x80000000 即 Integer.MIN_VALUE
     * 最大值为 0x7fffffff 即 Integer.MAX_VALUE
     *
     * @return 优先级顺序
     */
    int value() default NumberConstant.ZERO;
}
