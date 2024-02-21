package com.gitee.usl.infra.enums;

import com.gitee.usl.api.annotation.Description;

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
     * 共享变量尚未初始化
     */
    EMPTY_SHARED_SESSION(501),

    /**
     * 编译失败
     */
    COMPILE_FAILURE(510),

    @Description("空脚本")
    SCRIPT_EMPTY(511),

    @Description("非法变量名")
    ILLEGAL_NAME(512),

    @Description("变量定义未使用指定符号 : =")
    NOT_USE_EQUAL_WHEN_DEFINE(513),

    @Description("未给变量进行任何赋值操作")
    NOT_ASSIGN_VALUE_TO_DEFINE(514),

    @Description("变量定义与复制语句缺少结束标识")
    NOT_END_FOR_DEFINE_ASSIGN(515),

    @Description("无法被执行的语句，例如 Return 后的代码块")
    NOT_EXECUTE_CODE(516),

    @Description("For循环中变量定义超过两个")
    TOO_MANY_VARIABLE_IN_LOOP(517),

    @Description("FOR循环中未使用IN关键字")
    NOT_USE_IN_WITH_FOR(518),

    @Description("FOR循环对象非可循环类型")
    NOT_LOOP_TYPE_WITH_FOR(519),

    @Description("缺少左侧大括号")
    NOT_BIG_BRACKET_ON_LEFT(520),

    @Description("缺少右侧大括号")
    NOT_BIG_BRACKET_ON_RIGHT(521),

    @Description("不支持的值类型")
    NOT_SUPPORT_VALUE_TYPE(522),

    @Description("Lambda匿名函数不存在")
    NOT_FOUND_OF_LAMBDA(523),

    @Description("函数参数个数不匹配")
    NOT_MATCH_OF_ARGUMENT_COUNT(524),

    @Description("等待超时")
    TIMEOUT(600);

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
