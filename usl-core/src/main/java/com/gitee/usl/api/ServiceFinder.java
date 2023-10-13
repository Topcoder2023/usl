package com.gitee.usl.api;

import java.util.List;

/**
 * 自定义服务发现者
 * 用以替代默认的 SPI 服务发现者
 * 在对接 Spring 时，可以使用 ApplicationContext.getBean(serviceType) 获取容器中的服务
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
    <T> List<T> findAll(Class<T> serviceType);
}
