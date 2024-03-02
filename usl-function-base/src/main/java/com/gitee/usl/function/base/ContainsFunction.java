package com.gitee.usl.function.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.util.Collection;

/**
 * @author hongda.li
 */
@FunctionGroup
public class ContainsFunction {

    @Function("contains")
    public boolean contains(Object container, Object element) {
        return ObjectUtil.contains(container, element);
    }

    @Function("contains_all")
    public boolean containsAll(Collection<?> left, Collection<?> right) {
        return CollUtil.containsAll(left, right);
    }

    @Function("contains_any")
    public boolean containsAny(Collection<?> left, Collection<?> right) {
        return CollUtil.containsAny(left, right);
    }

    @Function("contains_none")
    public boolean containsNone(Collection<?> left, Collection<?> right) {
        return !CollUtil.containsAny(left, right);
    }

}
