package com.gitee.usl.kernel.provider;

import com.gitee.usl.api.Definable;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Notes;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.constant.ModuleConstant;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.EngineConfig;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.api.FunctionProvider;
import com.google.auto.service.AutoService;

import java.util.List;
import java.util.Set;

/**
 * USL 函数定义信息提供器管理者
 * 负责加载函数定义信息提供者
 * 并根据脚本引擎的配置提供全部的函数信息
 * 最后根据函数信息依次注册函数
 *
 * @author hongda.li
 */
@Notes(value = "函数工厂初始化器",
        belongs = ModuleConstant.USL_CORE,
        viewUrl = "https://gitee.com/yixi-dlmu/usl/raw/master/usl-core/src/main/java/com/gitee/usl/kernel/provider/FunctionProviderManager.java")
@Order(FunctionProviderManager.USL_FUNC_PROVIDER_ORDER)
@AutoService(Initializer.class)
public class FunctionProviderManager implements Initializer {
    /**
     * 函数提供者管理器的优先级
     */
    public static final int USL_FUNC_PROVIDER_ORDER = Integer.MIN_VALUE + 1000;

    @Override
    public void doInit(Configuration uslConfiguration) {
        EngineConfig configuration = uslConfiguration.getEngineConfig();

        // 函数容器
        final FunctionHolder holder = configuration.getFunctionHolder();

        // 加载所有函数定义信息提供者
        List<FunctionProvider> providers = ServiceSearcher.searchAll(FunctionProvider.class);

        // 根据函数提供者提供的函数定义信息依次注册函数
        providers.forEach(provider -> provider.provide(configuration).forEach(function -> {
            if (function instanceof Definable) {
                Set<String> alias = ((Definable) function).definition().alias();
                holder.register(function, alias);
            } else {
                holder.register(function);
            }
        }));
    }
}
