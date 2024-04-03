package com.manda.usl.http.server.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.smartboot.http.common.enums.HttpStatus;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
public class Result {

    private Integer code;

    private String message;

    private Object data;

    public static Result failure(int code) {
        return failure(code, null);
    }

    public static Result failure(String message) {
        return failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public static Result failure(int code, String message) {
        return new Result()
                .setCode(code)
                .setMessage(message);
    }

    public static Result success() {
        return new Result().setCode(HttpStatus.OK.value());
    }

    public static Result success(Object data) {
        return new Result().setCode(HttpStatus.OK.value()).setData(data);
    }
}
