package com.gitee.usl.kernel.domain;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.infra.enums.ResultCode;

import java.util.StringJoiner;

/**
 * 执行结果
 *
 * @author hongda.li
 */
public class Result<T> {
    /**
     * 返回值
     */
    private final T data;

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 消息描述
     */
    private final String message;

    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS, CharSequenceUtil.EMPTY, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, CharSequenceUtil.EMPTY, data);
    }

    public static <T> Result<T> failure() {
        return new Result<>(ResultCode.FAILURE, CharSequenceUtil.EMPTY, null);
    }

    public static <T> Result<T> failure(ResultCode resultCode) {
        return new Result<>(resultCode, CharSequenceUtil.EMPTY, null);
    }

    public static <T> Result<T> failure(ResultCode resultCode, String message) {
        return new Result<>(resultCode, message, null);
    }

    public Result(ResultCode resultCode, String message, T data) {
        this.data = data;
        this.message = message;
        this.code = resultCode.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Result.class.getSimpleName() + "[", "]")
                .add("data=" + data)
                .add("code=" + code)
                .add("message='" + message + "'")
                .toString();
    }
}
