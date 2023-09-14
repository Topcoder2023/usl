package com.gitee.usl.infra.enums;

/**
 * @author hongda.li
 */
public enum ResultCode {
    /**
     * 执行成功
     */
    SUCCESS(100),

    /**
     * 执行失败
     */
    FAILURE(500),

    /**
     * 编译失败
     */
    COMPILE_FAILURE(510),;

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
