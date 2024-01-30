package com.gitee.usl.kernel.engine;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.AttributeMeta;
import com.gitee.usl.infra.structure.StringSet;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author hongda.li
 */
@Data
@Description("函数定义")
@Accessors(chain = true)
public class FunctionDefinition {

    @Description("函数名称")
    private final String name;

    @Description("USL执行器")
    private final USLRunner runner;

    @Description("方法元数据")
    private MethodMeta<?> methodMeta;

    @Description("函数别名映射")
    private final StringSet alias = new StringSet();

    @Description("函数元数据")
    private final AttributeMeta attribute = new AttributeMeta();

    public FunctionDefinition(String name, USLRunner runner) {
        this.name = name;
        this.runner = runner;
    }

    public FunctionDefinition(String name, USLRunner runner, MethodMeta<?> methodMeta) {
        this.name = name;
        this.runner = runner;
        this.methodMeta = methodMeta;
    }

    public void addAlias(String... names) {
        this.alias.addAll(Arrays.asList(names));
    }

    @Description("获取形参长度")
    public int getArgsLength() {
        return Optional.ofNullable(methodMeta)
                .map(MethodMeta::method)
                .map(Method::getParameterCount)
                .orElse(NumberConstant.ZERO);
    }

    @Description("函数名称是否匹配")
    public boolean matchName(String name) {
        return this.name.equals(name) || this.alias.contains(name);
    }

}
