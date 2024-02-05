package com.gitee.usl.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 长度校验注解
 *
 * @author jingshu.zeng
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Length {

    /**
     * 确切的长度
     *
     * @return 长度值
     */
    int exactLength() default -1;

    /**
     * 最小长度
     *
     * @return 最小长度值，默认为不限制最小长度
     */
    int minLength() default -1;  // 默认为-1，表示不限制最小长度

    /**
     * 最大长度
     *
     * @return 最大长度值，默认为不限制最大长度
     */
    int maxLength() default -1;  // 默认为-1，表示不限制最大长度

    /**
     * 错误消息
     *
     * @return 错误消息
     */
    String message() default "参数 [{name}] 的第 [{index}] 个长度校验失败，实际值为 [{value}]"
            + "{最小长度为 [{minLength}]}"
            + "{期望长度为 [{exactLength}]}"
            + "{最大长度为 [{maxLength}]}";
}
