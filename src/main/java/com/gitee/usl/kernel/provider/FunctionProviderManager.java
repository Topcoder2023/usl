package com.gitee.usl.kernel.provider;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.domain.FunctionHolder;
import com.gitee.usl.kernel.engine.FunctionProvider;
import com.google.auto.service.AutoService;

import java.util.List;

/**
 * USL 函数定义信息提供器管理者
 * 负责加载函数定义信息提供者
 * 并根据脚本引擎的配置提供全部的函数信息
 * 最后根据函数信息依次注册函数
 *
 * @author hongda.li
 */
@Order(FunctionProviderManager.USL_FUNC_PROVIDER_ORDER)
@AutoService(Initializer.class)
public class FunctionProviderManager implements Initializer {
    /**
     * 函数提供者管理器的优先级
     */
    public static final int USL_FUNC_PROVIDER_ORDER = Integer.MIN_VALUE + 1000;

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        EngineConfiguration configuration = uslConfiguration.getEngineConfiguration();

        // 函数容器
        final FunctionHolder holder = configuration.getFunctionHolder();

        // 加载所有函数定义信息提供者
        List<FunctionProvider> providers = SpiServiceUtil.services(FunctionProvider.class);

        // 根据函数提供者提供的函数定义信息依次注册函数
        providers.forEach(provider -> provider.provide(configuration).forEach(holder::register));
    }
}
