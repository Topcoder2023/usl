package com.gitee.usl.kernel.domain;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.infra.enums.ResultCode;

/**
 * 执行结果
 *
 * @author hongda.li
 */
public class UslResult<T> {
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

    public static <T> UslResult<T> success() {
        return new UslResult<>(ResultCode.SUCCESS, CharSequenceUtil.EMPTY, null);
    }

    public static <T> UslResult<T> failure() {
        return new UslResult<>(ResultCode.FAILURE, CharSequenceUtil.EMPTY, null);
    }

    public UslResult(ResultCode resultCode, String message, T data) {
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
        return "Result{" +
                "data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
