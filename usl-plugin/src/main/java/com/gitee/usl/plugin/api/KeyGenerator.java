package com.gitee.usl.plugin.api;

import com.gitee.usl.infra.proxy.Invocation;

/**
 * @author hongda.li
 */
public interface KeyGenerator {
    /**
     * 根据本次调用信息生成唯一键
     *
     * @param name       函数名称
     * @param invocation 调用信息
     * @return 缓存键
     */
    String generateKey(String name, Invocation<?> invocation);
}
