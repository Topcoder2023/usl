package com.gitee.usl.api.impl;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.text.StrPool;
import com.gitee.usl.api.FunctionFilter;
import com.gitee.usl.grammar.runtime.type._Function;

/**
 * 默认的函数过滤器
 *
 * @author hongda.li
 */
public class DefaultFunctionFilter implements FunctionFilter {

    /**
     * 匹配规则
     */
    protected final String pattern;

    /**
     * 函数名称匹配器
     */
    protected final AntPathMatcher pathMatcher = new AntPathMatcher(StrPool.UNDERLINE);

    public DefaultFunctionFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean allowedRegister(_Function function) {
        return this.matched(this.pattern, function);
    }

    protected boolean matched(String pattern, _Function function) {
        return pathMatcher.match(pattern, function.name());
    }
}
