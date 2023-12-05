package com.gitee.usl.infra.enums;

/**
 * @author hongda.li
 */
public enum InteractiveMode {
    /**
     * 空交互
     * 即不采用任何操作
     */
    NONE,

    /**
     * CLI 命令行交互
     * Command-Line Interface
     */
    CLI,

    /**
     * Web Server 交互
     * 即采用 HTTP 协议访问函数
     */
    WEB;

    public static InteractiveMode of(String modeName) {
        for (InteractiveMode mode : values()) {
            if (mode.name().equalsIgnoreCase(modeName)) {
                return mode;
            }
        }
        return NONE;
    }
}
