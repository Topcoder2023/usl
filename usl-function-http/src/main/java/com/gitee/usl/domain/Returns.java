package com.gitee.usl.domain;

import com.gitee.usl.infra.constant.StringConstant;
import lombok.Getter;
import lombok.ToString;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
@Getter
@ToString
public class Returns {

    private String code;

    private String message;

    private Object data;

    public static Returns success() {
        return new Returns().setCode(StringConstant.SUCCESS);
    }

    public static Returns success(Object data) {
        return new Returns().setCode(StringConstant.SUCCESS).setData(data);
    }

    public static Returns failure() {
        return new Returns().setCode(StringConstant.FAILURE);
    }

    public static Returns failure(String message) {
        return new Returns().setCode(StringConstant.FAILURE).setMessage(message);
    }

    public Returns setCode(String code) {
        this.code = code;
        return this;
    }

    public Returns setMessage(String message) {
        this.message = message;
        return this;
    }

    public Returns setData(Object data) {
        this.data = data;
        return this;
    }
}
