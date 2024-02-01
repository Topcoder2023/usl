package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.FunctionPluggable;
import com.gitee.usl.api.annotation.SystemComponent;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.infra.utils.AnnotatedComparator;

import java.util.Comparator;

/**
 * 插件重排序函数增强器
 * 该增强器会将所有函数插件按照插件上的 @Order 注解顺序进行重排序
 * 若插件上不存在 @Order 注解，则优先级默认为 0
 *
 * @author hongda.li
 */
@SystemComponent
public class ResortPluginEnhancer implements FunctionEnhancer {
    @Override
    public void enhance(_Function function) {
        if (function instanceof FunctionPluggable fp) {
            fp.plugins().sort(Comparator.comparing(plugin -> AnnotatedComparator.getOrder(plugin.getClass())));
        }
    }
}
