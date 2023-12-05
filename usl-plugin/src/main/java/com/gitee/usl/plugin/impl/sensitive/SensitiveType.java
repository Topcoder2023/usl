package com.gitee.usl.plugin.impl.sensitive;

/**
 * @author hongda.li
 */
public enum SensitiveType {
    /**
     * 用户 id
     */
    USER_ID,

    /**
     * 中文名
     */
    CHINESE_NAME,

    /**
     * 身份证号
     */
    ID_CARD,

    /**
     * 座机号
     */
    FIXED_PHONE,

    /**
     * 手机号
     */
    MOBILE_PHONE,

    /**
     * 地址
     */
    ADDRESS,

    /**
     * 电子邮件
     */
    EMAIL,

    /**
     * 密码
     */
    PASSWORD,

    /**
     * 中国大陆车牌，包含普通车辆、新能源车辆
     */
    CAR_LICENSE,

    /**
     * 银行卡
     */
    BANK_CARD,

    /**
     * IPv4地址
     */
    IPV4,

    /**
     * IPv6地址
     */
    IPV6,

    /**
     * 定义了一个 first_mask 的规则，只显示第一个字符。
     */
    FIRST_MASK,

    /**
     * 清空为 null
     */
    CLEAR_TO_NULL,

    /**
     * 清空为 ""
     */
    CLEAR_TO_EMPTY,

    /**
     * 自定义规则 1
     */
    CUSTOM_01,

    /**
     * 自定义规则 2
     */
    CUSTOM_02,

    /**
     * 自定义规则 3
     */
    CUSTOM_03,

    /**
     * 自定义规则 4
     */
    CUSTOM_04,

    /**
     * 自定义规则 5
     */
    CUSTOM_05,

    /**
     * 自定义规则 6
     */
    CUSTOM_06,

    /**
     * 自定义规则 7
     */
    CUSTOM_07,

    /**
     * 自定义规则 8
     */
    CUSTOM_08,

    /**
     * 自定义规则 9
     */
    CUSTOM_09,

    /**
     * 自定义规则 10
     */
    CUSTOM_10,

    /**
     * 自定义规则 11
     */
    CUSTOM_11,

    /**
     * 自定义规则 12
     */
    CUSTOM_12,

    /**
     * 自定义规则 13
     */
    CUSTOM_13,

    /**
     * 自定义规则 14
     */
    CUSTOM_14,

    /**
     * 自定义规则 15
     */
    CUSTOM_15,

    /**
     * 自定义规则 16
     */
    CUSTOM_16,

    /**
     * 自定义规则 17
     */
    CUSTOM_17,

    /**
     * 自定义规则 18
     */
    CUSTOM_18,

    /**
     * 自定义规则 19
     */
    CUSTOM_19,

    /**
     * 自定义规则 20
     */
    CUSTOM_20;
}
