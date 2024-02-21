package com.gitee.usl.function.ai.infra;

import cn.hutool.setting.dialect.Props;

/**
 * @author hongda.li
 */
public class ApiConfigHelper {
    private static final Props CONFIG = new Props("api-config.properties");

    private ApiConfigHelper() {
    }

    public static String getKey(ChatType chatType) {
        return CONFIG.getStr("chat" + chatType.getCode() + ".key");
    }

}
