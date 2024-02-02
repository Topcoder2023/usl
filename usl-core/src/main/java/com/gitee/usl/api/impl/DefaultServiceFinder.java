package com.gitee.usl.api.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import com.gitee.usl.api.ServiceFinder;

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
            return ReflectUtil.newInstanceIfPossible(serviceType);
        } else {
            // 尝试通过SPI机制加载第一个类
            return ServiceLoaderUtil.loadFirstAvailable(serviceType);
        }
    }
}
