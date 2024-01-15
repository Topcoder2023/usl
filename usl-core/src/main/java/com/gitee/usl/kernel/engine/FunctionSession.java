package com.gitee.usl.kernel.engine;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.Function;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
@Description("函数执行会话")
public final class FunctionSession {

    @Description("函数执行结果")
    private Object result;

    @Description("上下文参数")
    private final Env env;

    @Description("函数异常信息")
    private Exception exception;

    @Description("函数调用信息")
    private Invocation<?> invocation;

    @Description("函数参数")
    private final AviatorObject[] objects;

    @Description("函数定义信息")
    private final FunctionDefinition definition;

    @Description("实际的处理逻辑")
    private Function<FunctionSession, Object> handler;

    public FunctionSession(Env env, AviatorObject[] objects, FunctionDefinition definition) {
        this.env = env;
        this.objects = objects;
        this.definition = definition;
    }

}
