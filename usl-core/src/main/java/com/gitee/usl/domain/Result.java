package com.gitee.usl.domain;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.structure.BasicConverter;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Data
@Description("执行结果")
@Accessors(chain = true)
public class Result implements BasicConverter {

    @Description("返回值")
    private final Object data;

    @Description("状态码")
    private final Integer code;

    @Description("消息描述")
    private final String message;
    @Description("异常信息")
    private final Exception exception;

    public static Result empty() {
        return new Result(ResultCode.SUCCESS, null, null, null);
    }

    public static Result success() {
        return new Result(ResultCode.SUCCESS, CharSequenceUtil.EMPTY, null, null);
    }

    public static Result success(Object data) {
        return new Result(ResultCode.SUCCESS, CharSequenceUtil.EMPTY, data, null);
    }

    public static Result failure() {
        return new Result(ResultCode.FAILURE, CharSequenceUtil.EMPTY, null, null);
    }

    public static Result failure(ResultCode resultCode) {
        return new Result(resultCode, CharSequenceUtil.EMPTY, null, null);
    }

    public static Result failure(ResultCode resultCode, String message) {
        return new Result(resultCode, message, null, null);
    }

    public static Result failure(ResultCode resultCode, Exception exception) {
        return new Result(resultCode, null, null, exception);
    }

    public static Result failure(ResultCode resultCode, String message, Exception exception) {
        return new Result(resultCode, message, null, exception);
    }

    public Result(ResultCode resultCode, String message, Object data, Exception exception) {
        this.data = data;
        this.message = message;
        this.code = resultCode.code();
        this.exception = exception;
    }

    @Override
    public Object origin() {
        return this.data;
    }

}
