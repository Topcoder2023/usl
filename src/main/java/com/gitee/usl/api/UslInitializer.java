package com.gitee.usl.api;

import com.gitee.usl.kernel.configure.UslConfiguration;

/**
 * 初始化器
 *
 * @author hongda.li
 */
@FunctionalInterface
public interface UslInitializer {
    /**
     * 执行初始化逻辑
     *
     * @param configuration USL 配置类
     */
    void doInit(UslConfiguration configuration);
}
