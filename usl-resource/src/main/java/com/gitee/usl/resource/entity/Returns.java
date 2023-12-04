package com.gitee.usl.resource.entity;

import com.gitee.usl.infra.constant.StringConstant;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
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

    public String getCode() {
        return code;
    }

    public Returns setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Returns setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Returns setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Returns.class.getSimpleName() + "[", "]")
                .add("flag='" + code + "'")
                .add("message='" + message + "'")
                .add("data=" + data)
                .toString();
    }
}
