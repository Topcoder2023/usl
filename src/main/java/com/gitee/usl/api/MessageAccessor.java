package com.gitee.usl.api;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface MessageAccessor {
    /**
     * 解析并获取消息
     *
     * @param code 消息编码
     * @param args 消息参数
     * @return 解析后的消息
     */
    String message(String code, Object... args);

    /**
     * 默认的消息解析器
     */
    MessageAccessor DEFAULT_MESSAGE_ACCESSOR = ((code, args) -> {
        if (CharSequenceUtil.isBlank(code)
                || ArrayUtil.isEmpty(args)
                || !CharSequenceUtil.contains(code, StrPool.EMPTY_JSON)) {
            return code;
        }

        return CharSequenceUtil.format(code, args);
    });
}
