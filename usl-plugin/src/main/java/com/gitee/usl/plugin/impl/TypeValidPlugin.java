package com.gitee.usl.plugin.impl;

import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.Type;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

/**
 * 参数类型校验插件
 *
 * @author jingshu.zeng
 */
public class TypeValidPlugin extends AbstractValidPlugin<Type> {

    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, Type annotation, Object actual) {
        // 获取预期参数类型
        Class<?> expectedType = annotation.value();

        // 如果未指定具体类型，则使用当前参数的类型作为预期参数类型
        if (expectedType == Class.class) {
            expectedType = actual.getClass();
        }

        // 实际参数类型
        Class<?> actualType = actual.getClass();

        // 参数类型校验
        if (!expectedType.isAssignableFrom(actualType)) {
            // 构造错误消息
            String message = annotation.message()
                    .replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()))
                    .replace("{expected}", expectedType.getSimpleName())
                    .replace("{actual}", actualType.getSimpleName());

            // 抛出校验异常
            throw new USLValidException(message, location, actual, expectedType);
        }
    }
}
