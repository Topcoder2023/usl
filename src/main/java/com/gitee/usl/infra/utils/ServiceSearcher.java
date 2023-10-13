package com.gitee.usl.infra.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ServiceLoaderUtil;
import com.gitee.usl.infra.constant.NumberConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SPI 服务发现机制工具类
 *
 * @author hongda.li
 */
public class ServiceSearcher {
    public static final String DISABLE_SERVICE = "usl.disable.service";
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSearcher.class);
    private static final Set<String> EXCLUDE_TYPES = HashSet.newHashSet(NumberConstant.COMMON_SIZE);
    private static final AtomicBoolean INIT_FLAG = new AtomicBoolean(false);

    private ServiceSearcher() {
    }

    /**
     * 快速禁用指定的 SPI 服务
     * 此方法需要在 start() 方法前调用
     * 否则可能会导致服务禁用失败
     *
     * @param services 指定服务列表
     */
    public void disable(Class<?>... services) {
        String disabledName = Stream.of(services)
                .map(Class::getName)
                .collect(Collectors.joining(StrPool.COMMA));

        String property = System.getProperty(DISABLE_SERVICE);
        if (property == null) {
            System.setProperty(DISABLE_SERVICE, disabledName);
        } else {
            System.setProperty(DISABLE_SERVICE, property + StrPool.COMMA + disabledName);
        }
    }

    /**
     * 初始化服务查找器
     * 读取系统环境变量中的被禁用服务
     * 并保存这些被禁用服务的全类名
     */
    private static void initSelf() {
        if (INIT_FLAG.get()) {
            return;
        }
        EXCLUDE_TYPES.addAll(Optional.ofNullable(System.getProperty(DISABLE_SERVICE))
                .map(names -> ((Set<String>) new HashSet<>(CharSequenceUtil.split(names, StrPool.COMMA))))
                .orElse(Collections.emptySet()));
        INIT_FLAG.compareAndSet(false, true);
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
        // 初始化服务
        initSelf();

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
