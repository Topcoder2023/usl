package com.gitee.usl.kernel.engine;

import com.gitee.usl.infra.proxy.Invocation;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.util.Arrays;
import java.util.StringJoiner;

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

    public FunctionSession setInvocation(Invocation<?> invocation) {
        this.invocation = invocation;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FunctionSession.class.getSimpleName() + "[", "]")
                .add("result=" + result)
                .add("exception=" + exception)
                .add("env=" + env)
                .add("invocation=" + invocation)
                .add("objects=" + Arrays.toString(objects))
                .add("definition=" + definition)
                .toString();
    }
}
