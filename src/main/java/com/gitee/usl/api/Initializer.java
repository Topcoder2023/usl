package com.gitee.usl.api;

/**
 * 初始化器
 *
 * @author hongda.li
 */
@FunctionalInterface
public interface Initializer {
    /**
     * 执行初始化逻辑
     */
    void doInit();
}
