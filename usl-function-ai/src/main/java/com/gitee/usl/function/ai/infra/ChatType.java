package com.gitee.usl.function.ai.infra;

import lombok.Getter;

/**
 * @author hongda.li
 */
@Getter
public enum ChatType {

    ZHI_PU_4(100, "https://open.bigmodel.cn/api/paas/v4/chat/completions");

    private final int code;

    private final String path;

    ChatType(int code, String path) {
        this.code = code;
        this.path = path;
    }

    public static ChatType of(int code) {
        for (ChatType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
