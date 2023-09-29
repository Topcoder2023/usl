package com.gitee.usl.kernel.engine;

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
    private final AviatorObject[] objects;
    private final FunctionDefinition definition;

    public FunctionSession(Env env, AviatorObject[] objects, FunctionDefinition definition) {
        this.env = env;
        this.objects = objects;
        this.definition = definition;
    }

    public Env getEnv() {
        return env;
    }

    public Exception getException() {
        return exception;
    }

    public FunctionSession setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public FunctionSession setResult(Object result) {
        this.result = result;
        return this;
    }

    public AviatorObject[] getObjects() {
        return objects;
    }

    public FunctionDefinition getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FunctionSession.class.getSimpleName() + "[", "]")
                .add("env=" + env)
                .add("objects=" + Arrays.toString(objects))
                .add("definition=" + definition)
                .add("result=" + result)
                .toString();
    }
}
