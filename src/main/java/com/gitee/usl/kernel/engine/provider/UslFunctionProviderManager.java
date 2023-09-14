package com.gitee.usl.kernel.engine.provider;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.infra.exception.UslNotFoundException;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.engine.UslFunctionFactory;
import com.gitee.usl.kernel.engine.UslFunctionProvider;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * USL 函数定义信息提供器管理者
 * 负责加载函数定义信息提供者
 * 并根据脚本引擎的配置提供全部的函数信息
 * 最后根据函数信息依次注册函数
 *
 * @author hongda.li
 */
public class UslFunctionProviderManager implements Initializer {
    private static final String NOT_FOUND = "There is no function factory that can support the creation of this type. [{}]";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        EngineConfiguration configuration = uslConfiguration.getEngineConfiguration();

        // 函数工厂
        final List<UslFunctionFactory> factoryList = configuration.getFactoryList();

        // 函数集合
        final Map<String, AviatorFunction> functionMap = configuration.getFunctionMap();

        // 加载所有函数定义信息提供者
        List<UslFunctionProvider> providers = SpiServiceUtil.services(UslFunctionProvider.class);

        for (UslFunctionProvider provider : providers) {
            logger.info("USL function provider found - [{}]", provider.getClass().getName());

            // 根据函数提供者提供的函数定义信息依次注册函数
            provider.provide(configuration).forEach(definition -> {
                String name = definition.getName();

                logger.info("Register USL function - [{}]", name);

                // 跳过重名函数
                if (functionMap.containsKey(name)) {
                    logger.warn("USL function has been registered - [{}]", name);
                    return;
                }

                // 先根据函数工厂是否支持函数定义来过滤
                // 再根据函数工厂优先级获取首个可用工厂
                // 再通过工厂创建函数
                // 当找不到合适的函数工厂或创建错误时，抛出异常
                AviatorFunction function = factoryList.stream()
                        .filter(factory -> factory.supported(definition))
                        .findFirst()
                        .map(factory -> factory.create(definition))
                        .orElseThrow(() -> new UslNotFoundException(NOT_FOUND, definition));

                functionMap.put(name, function);
            });
        }
    }
}
