package com.gitee.usl.infra.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ServiceLoaderUtil;
import com.gitee.usl.api.ServiceFinder;
import com.gitee.usl.api.annotation.Description;
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
    /**
     * 待禁止服务的系统属性的名称
     * 待禁止服务的全类名存储在 excludeServiceNames 变量中
     */
    public static final String DISABLE_SERVICE = "usl.disabled.service";
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSearcher.class);

    /**
     * ServiceSearcher 初始化标识
     * 初始化方法会在第一次调用 searchAll() 方法时被懒加载调用
     * 这样的好处是将 excludeServiceNames 变量和 serviceFinder 变量的初始化尽可能延后
     * 以便于其它调用方扩展它们
     * 此处使用 AtomicBoolean 类型，当返回值为 true 时，表示 ServiceSearcher 已初始化完成
     */
    private static final AtomicBoolean INIT_FLAG = new AtomicBoolean(false);

    /**
     * 需要排除的服务的全类名集合
     * 当调用方不希望某些扩展服务实现类被使用时
     * 可以通过调用 disable() 方法并传入需要禁止的服务类
     * 也可以通过设置 System.setProperty(DISABLE_SERVICE, "serviceName") 来实现同样的目的
     */
    private static Set<String> excludeServiceNames;

    /**
     * 服务发现者
     * 用以发现指定类型的服务实现
     * 默认使用 JDK 内置的 SPI 服务发现机制
     * 即在 META-INF/services/目录下创建服务接口的全类名文件
     * 并在文件中声明所有可直接实例化的具有无参构造器的实现类的全类名
     * 此处变量的声明是为了自定义拓展服务发现机制
     * 例如，当 Spring 项目可以使用 (type) -> ApplicationContext.getBean(type) 来实现服务发现
     * 也可以将两者组合起来，先从容器中获取组件，再基于 SPI 获取组件
     */
    private static ServiceFinder serviceFinder;

    private ServiceSearcher() {
    }

    /**
     * 快速禁用指定的服务
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
        excludeServiceNames = Optional.ofNullable(System.getProperty(DISABLE_SERVICE))
                .map(names -> ((Set<String>) new HashSet<>(CharSequenceUtil.split(names, StrPool.COMMA))))
                .orElse(Collections.emptySet());
        serviceFinder = Optional.ofNullable(ServiceLoaderUtil.loadFirst(ServiceFinder.class)).orElse(ServiceLoaderUtil::loadList);

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
        List<T> elements = serviceFinder.findAll(serviceType)
                .stream()
                .filter(element -> !excludeServiceNames.contains(element.getClass().getName()))
                .collect(Collectors.toList());

        if (CollUtil.isEmpty(elements)) {
            return Collections.emptyList();
        }

        // 若服务不为空则排序后返回
        AnnotatedComparator.sort(elements);

        elements.forEach(item -> LOGGER.debug("SPI服务实例 - [{} & {}]", serviceType.getName(), item.getClass().getName()));

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

        @Description("加载所有可用服务并排序")
        Iterator<T> iterator = ServiceSearcher.searchAll(serviceType).iterator();

        return iterator.hasNext() ? iterator.next() : null;
    }
}
