package com.gitee.usl.api;

import com.gitee.usl.infra.enums.ValidatorType;
import com.gitee.usl.kernel.engine.FunctionSession;

import java.lang.annotation.Annotation;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface Validator<A extends Annotation> {
    /**
     * 执行校验逻辑
     *
     * @param annotation 校验的目标注解
     * @param session    本次调用的请求会话
     */
    void validate(A annotation, FunctionSession session);

    /**
     * 校验器的类型
     * 默认为参数校验器
     *
     * @return 校验器的类型
     */
    default ValidatorType type() {
        return ValidatorType.ARGUMENTS;
    }
}
