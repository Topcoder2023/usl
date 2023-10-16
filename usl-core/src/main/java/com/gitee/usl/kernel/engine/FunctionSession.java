package com.gitee.usl.kernel.engine;

import com.gitee.usl.infra.proxy.Invocation;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * 当前函数执行会话
 * 保存了当前调用的信息
 * 即上下文环境、脚本内容、调用参数
 * 以及持有 函数调用信息 和 函数定义信息的 引用
 *
 * @author hongda.li
 */
public final class FunctionSession {
    private Object result;
    private Exception exception;
    private final Env env;
    private Invocation<?> invocation;
    private final AviatorObject[] objects;
    private final FunctionDefinition definition;
    /**
     * 实际的处理逻辑
     * 即插件化 FunctionPluggable 接口的 handle() 方法
     * 暴露出来便于部分插件在 onBegin() 时提前执行实际处理逻辑
     */
    private Function<FunctionSession, Object> handler;

    public FunctionSession(Env env, AviatorObject[] objects, FunctionDefinition definition) {
        this.env = env;
        this.objects = objects;
        this.definition = definition;
    }

    public Env env() {
        return env;
    }

    public Exception exception() {
        return exception;
    }

    public FunctionSession setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public Object result() {
        return result;
    }

    public FunctionSession setResult(Object result) {
        this.result = result;
        return this;
    }

    public AviatorObject[] objects() {
        return objects;
    }

    public FunctionDefinition definition() {
        return definition;
    }

    public Invocation<?> invocation() {
        return invocation;
    }

    public void setInvocation(Invocation<?> invocation) {
        this.invocation = invocation;
    }

    public Function<FunctionSession, Object> handler() {
        return handler;
    }

    public void setHandler(Function<FunctionSession, Object> handler) {
        this.handler = handler;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FunctionSession.class.getSimpleName() + "[", "]")
                .add("result=" + result)
                .add("exception=" + exception)
                .add("definition=" + definition)
                .toString();
    }
}
