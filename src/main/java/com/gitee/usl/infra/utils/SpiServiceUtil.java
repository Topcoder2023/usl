package com.gitee.usl.infra.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * SPI 服务发现机制工具类
 *
 * @author hongda.li
 */
public class SpiServiceUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpiServiceUtil.class);

    private SpiServiceUtil() {
    }

    /**
     * 根据SPI机制加载所有可用服务并排序
     * 默认按正序即从小到大的顺序排序
     *
     * @param serviceType 服务类型
     * @param <T>         服务泛型
     * @return 服务实现集合
     */
    public static <T> List<T> loadSortedService(Class<T> serviceType) {
        // 根据SPI机制加载所有可用服务
        List<T> elements = ServiceLoaderUtil.loadList(serviceType);

        if (CollUtil.isEmpty(elements)) {
            return Collections.emptyList();
        }

        // 若服务不为空则排序后返回
        CompareUtil.sort(elements);

        elements.forEach(item -> LOGGER.debug("Spi Service found. [{} - {}]", serviceType.getName(), item.getClass().getName()));

        return elements;
    }

    /**
     * 根据SPI机制加载所有可用服务并排序
     * 并返回首个可用的服务实现
     * 默认按正序即从小到大的顺序排序
     *
     * @param serviceType 服务类型
     * @param <T>         服务泛型
     * @return 首个可用的服务实现
     */
    public static <T> T loadFirstSortedService(Class<T> serviceType) {
        // 加载所有可用服务并排序
        Iterator<T> iterator = SpiServiceUtil.loadSortedService(serviceType).iterator();

        // 若服务不为空则返回首个可用服务
        return iterator.hasNext() ? iterator.next() : null;
    }
}
