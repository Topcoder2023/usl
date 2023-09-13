package com.gitee.usl.infra.proxy;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.exception.UslException;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.UslConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * USL 动态代理管理器
 *
 * @author hongda.li
 */
@Order
public class UslProxyManager implements Initializer {
    /**
     * 当被代理的类不是接口时的报错信息
     */
    private static final String PROXY_FAILURE = "The target being proxied must be an interface. {}";

    /**
     * 当动态代理类创建成功时的报错信息
     */
    private static final String PROXY_SUCCESS = "Successfully created proxy for class {}";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doInit(UslConfiguration configuration) {
        SpiServiceUtil.loadSortedService(UslProxy.class).forEach(this::initProxy);
    }

    /**
     * 初始化指定 USL 动态代理
     * 创建代理实例并保存
     *
     * @param uslProxy USL 动态代理实现
     */
    private void initProxy(UslProxy uslProxy) {
        uslProxy.targetList().forEach(targeted -> {
            // 校验代理目标是否为接口
            String targetedName = targeted.getName();
            Assert.isTrue(targeted.isInterface(), () -> new UslException(PROXY_FAILURE, targetedName));

            // 基于JDK的动态代理机制创建代理实例
            ClassLoader classLoader = ClassUtil.getClassLoader();
            InvocationHandler handler = (proxy, method, args) -> uslProxy.doProxy(targeted, method, args);
            Object proxyInstance = Proxy.newProxyInstance(classLoader, new Class[]{targeted}, handler);

            // 保存动态代理实例
            Singleton.put(targetedName, proxyInstance);

            logger.info(PROXY_SUCCESS, targetedName);
        });
    }
}
