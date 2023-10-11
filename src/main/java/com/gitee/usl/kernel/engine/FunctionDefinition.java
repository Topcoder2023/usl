package com.gitee.usl.kernel.engine;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.text.StrPool;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.AttributeMeta;

import java.util.*;

/**
 * USL 函数定义
 *
 * @author hongda.li
 */
public class FunctionDefinition {
    private final String name;
    private MethodMeta<?> methodMeta;
    private final AttributeMeta attribute = new AttributeMeta();
    private String url;

    public FunctionDefinition(String name) {
        this.name = name;
    }

    public MethodMeta<?> methodMeta() {
        return methodMeta;
    }

    public FunctionDefinition setMethodMeta(MethodMeta<?> methodMeta) {
        this.methodMeta = methodMeta;
        this.initUrl();
        return this;
    }

    public AttributeMeta attribute() {
        return attribute;
    }

    public String name() {
        return name;
    }

    public String url() {
        return url;
    }

    private void initUrl() {
        this.url = NamingCase.toSymbolCase(CharSequenceUtil.lowerFirst(this.methodMeta.targetType().getName()), CharPool.SLASH)
                + CharPool.SLASH
                + NamingCase.toSymbolCase(this.methodMeta.method().getName(), CharPool.SLASH);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FunctionDefinition.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("methodMeta=" + methodMeta)
                .add("attribute=" + attribute)
                .add("url=" + url)
                .toString();
    }
}
