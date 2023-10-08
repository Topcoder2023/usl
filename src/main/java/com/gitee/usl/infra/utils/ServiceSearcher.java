package com.gitee.usl.infra.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ServiceLoaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * SPI 服务发现机制工具类
 *
 * @author hongda.li
 */
public class ServiceSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSearcher.class);
    private static final String DISABLE_SERVICE = "usl.disable.service";
    private static final Set<String> EXCLUDE_TYPES;

    private ServiceSearcher() {
    }

    static {
        EXCLUDE_TYPES = Optional.ofNullable(System.getProperty(DISABLE_SERVICE))
                .map(names -> ((Set<String>) new HashSet<>(CharSequenceUtil.split(names, StrPool.COMMA))))
                .orElse(Collections.emptySet());

        EXCLUDE_TYPES.forEach(name -> LOGGER.info("Disable service by name - {}", name));
    }

    /**
     * 根据SPI机制加载所有可用服务并排序
     * 默认按正序即从小到大的顺序排序
     *
     * @param serviceType 服务类型
     * @param <T>         服务泛型
     * @return 服务实现集合
     */
    public static <T> List<T> searchAll(Class<T> serviceType) {
        // 根据SPI机制加载所有可用服务
        // 但排除指定的服务
        List<T> elements = new ArrayList<>(ServiceLoaderUtil.loadList(serviceType)
                .stream()
                .filter(element -> !EXCLUDE_TYPES.contains(element.getClass().getName()))
                .toList());

        if (CollUtil.isEmpty(elements)) {
            return Collections.emptyList();
        }

        // 若服务不为空则排序后返回
        AnnotatedComparator.sort(elements);

        elements.forEach(item -> LOGGER.debug("Spi Service found - [{} - {}]", serviceType.getName(), item.getClass().getName()));

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
    public static <T> T searchFirst(Class<T> serviceType) {
        // 加载所有可用服务并排序
        Iterator<T> iterator = ServiceSearcher.searchAll(serviceType).iterator();

        // 若服务不为空则返回首个可用服务
        return iterator.hasNext() ? iterator.next() : null;
    }
}
