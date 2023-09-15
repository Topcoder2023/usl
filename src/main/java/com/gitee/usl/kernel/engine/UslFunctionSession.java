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
public final class UslFunctionSession {
    private Object result;
    private String content;
    private Exception exception;
    private final Env env;
    private final AviatorObject[] objects;
    private final UslFunctionDefinition definition;

    public UslFunctionSession(Env env, AviatorObject[] objects, UslFunctionDefinition definition) {
        this.env = env;
        this.objects = objects;
        this.definition = definition;
    }

    public Env getEnv() {
        return env;
    }

    public String getContent() {
        return content;
    }

    public UslFunctionSession setContent(String content) {
        this.content = content;
        return this;
    }

    public Exception getException() {
        return exception;
    }

    public UslFunctionSession setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public UslFunctionSession setResult(Object result) {
        this.result = result;
        return this;
    }

    public AviatorObject[] getObjects() {
        return objects;
    }

    public UslFunctionDefinition getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UslFunctionSession.class.getSimpleName() + "[", "]")
                .add("env=" + env)
                .add("content='" + content + "'")
                .add("objects=" + Arrays.toString(objects))
                .add("definition=" + definition)
                .add("result=" + result)
                .toString();
    }
}
