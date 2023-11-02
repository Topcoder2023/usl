package com.gitee.usl.kernel.engine;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.AttributeMeta;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * USL 函数定义
 *
 * @author hongda.li
 */
public class FunctionDefinition {
    private final String name;
    private MethodMeta<?> methodMeta;
    private final AttributeMeta attribute = new AttributeMeta();
    private final Set<String> alias = new HashSet<>(NumberConstant.EIGHT);

    public FunctionDefinition(String name) {
        this.name = name;
    }

    public MethodMeta<?> methodMeta() {
        return methodMeta;
    }

    public FunctionDefinition setMethodMeta(MethodMeta<?> methodMeta) {
        this.methodMeta = methodMeta;
        return this;
    }

    public AttributeMeta attribute() {
        return attribute;
    }

    public String name() {
        return name;
    }

    public Set<String> alias() {
        return alias;
    }

    public void addAlias(String... names) {
        this.addAlias(null, names);
    }

    public void addAlias(String prefix, String... names) {
        this.alias.addAll(Stream.of(names)
                .map(str -> CharSequenceUtil.addPrefixIfNot(str, prefix))
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FunctionDefinition.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("methodMeta=" + methodMeta)
                .add("attribute=" + attribute)
                .toString();
    }
}
