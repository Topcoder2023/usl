package com.gitee.usl.infra.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    COMPILE_FAILURE(510),

    /**
     * 等待超时
     */
    TIMEOUT(520);

    private final Integer code;

    ResultCode(int code) {
        this.code = code;
    }

    public Integer code() {
        return code;
    }

    private static final Map<Integer, ResultCode> LOOK_UP;

    static {
        LOOK_UP = Stream.of(values()).collect(Collectors.toMap(ResultCode::code, Function.identity()));
    }

    public static ResultCode of(Integer code) {
        return LOOK_UP.get(code);
    }
}
