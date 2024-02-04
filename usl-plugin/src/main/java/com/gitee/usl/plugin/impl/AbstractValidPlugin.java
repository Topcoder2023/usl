package com.gitee.usl.plugin.impl;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.TypeUtil;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.domain.Location;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * 抽象注解校验器
 * 实现了注解校验的通用逻辑
 * 建议常规的注解校验器均继承此类
 *
 * @author hongda.li
 */
@SuppressWarnings("unchecked")
public abstract class AbstractValidPlugin<T extends Annotation> implements BeginPlugin {

    /**
     * 校验参数
     *
     * @param location   参数位置信息
     * @param annotation 注解
     * @param actual     参数值
     */
    protected abstract void valid(Location location, T annotation, Object actual);

    /**
     * 过滤出指定注解和实际参数
     *
     * @param session 函数会话
     */
    protected void filterAnnotation(FunctionSession session) {

        // 注解类型
        Class<T> target = (Class<T>) TypeUtil.getTypeArgument(this.getClass());

        // 原始参数
        _Object[] objects = session.getObjects();

        // 当前调用的实际参数
        Object[] values = session.getInvocation().args();

        // 方法的预期参数列表
        Parameter[] parameters = session.getDefinition().getMethodMeta().method().getParameters();

        // 跳过无参函数
        if (ArrayUtil.isEmpty(parameters)) {
            return;
        }

        // 参数索引
        IntWrapper index = new IntWrapper();

        Arrays.stream(parameters)
                // 依次获取每一个参数上的指定注解
                .map(parameter -> AnnotationUtil.getAnnotation(parameter, target))
                .forEach(annotation -> {
                    // 若注解不存在则跳过
                    if (annotation == null) {
                        index.increment();
                        return;
                    }

                    // 参数位置信息
                    Location location = Location.from(objects[index.get()].getFrom())
                            .setIndex(index.get() + 1)
                            .setName(session.getDefinition().getName());

                    // 若注解存在则执行校验
                    this.valid(location, annotation, values[index.get()]);

                    // 参数索引自增
                    index.increment();
                });
    }
}
