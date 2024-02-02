package com.gitee.usl.api;

/**
 * 服务发现者
 *
 * @author hongda.li
 */
@FunctionalInterface
public interface ServiceFinder {
    /**
     * 服务发现
     *
     * @param serviceType 服务类型
     * @return 可使用的服务
     */
    <T> T search(Class<T> serviceType);
}
