package com.gitee.usl.kernel.domain;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.enums.ResultCode;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Data
@Description("执行结果")
@Accessors(chain = true)
public class Result<T> {

    @Description("返回值")
    private final T data;

    @Description("状态码")
    private final Integer code;

    @Description("消息描述")
    private final String message;

    public static <T> Result<T> empty() {
        return new Result<>(ResultCode.SUCCESS, null, null);
    }

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
        this.code = resultCode.code();
    }

}
