package com.gitee.usl.api;

import com.github.rholder.retry.Retryer;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface RetryBuilderFactory {
    /**
     * 创建重试器
     *
     * @return 重试器
     */
    Retryer<Object> create();
}
