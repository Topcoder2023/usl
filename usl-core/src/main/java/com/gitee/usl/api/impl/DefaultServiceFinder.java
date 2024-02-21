package com.gitee.usl.api.impl;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import com.gitee.usl.api.ServiceFinder;

import java.util.Optional;

/**
 * 默认的服务发现者
 *
 * @author hongda.li
 */
public class DefaultServiceFinder implements ServiceFinder {
    @Override
    public <T> T search(Class<T> serviceType) {
        if (ClassUtil.isNormalClass(serviceType)) {
            // 尝试通过反射直接实例化一个类
            return Optional.ofNullable(ReflectUtil.newInstanceIfPossible(serviceType))
                    .map(instance -> {
                        // 确保此类是单例的
                        Singleton.put(instance);
                        return instance;
                    })
                    .orElse(null);
        } else {
            // 尝试通过SPI机制加载第一个类
            return ServiceLoaderUtil.loadFirstAvailable(serviceType);
        }
    }
}
