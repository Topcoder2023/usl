package com.gitee.usl.kernel.provider;

import com.gitee.usl.api.Definable;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.EngineConfig;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.api.FunctionLoader;
import com.google.auto.service.AutoService;

import java.util.List;
import java.util.Set;

/**
 * @author hongda.li
 */
@Description("函数定义信息提供器管理者")
@Order(FunctionProviderInitializer.USL_FUNC_PROVIDER_ORDER)
@AutoService(Initializer.class)
public class FunctionProviderInitializer implements Initializer {

    @Description("函数提供者管理器的优先级")
    public static final int USL_FUNC_PROVIDER_ORDER = Integer.MIN_VALUE + 1000;

    @Override
    public void doInit(Configuration configuration) {

        @Description("引擎配置类")
        EngineConfig engineConfig = configuration.getEngineConfig();

        @Description("函数容器")
        FunctionHolder holder = engineConfig.getFunctionHolder();

        @Description("函数定义加载器集合")
        List<FunctionLoader> providers = ServiceSearcher.searchAll(FunctionLoader.class);

        providers.forEach(provider -> provider.load(engineConfig).forEach(function -> {
            if (function instanceof Definable) {
                Set<String> alias = ((Definable) function).definition().getAlias();
                holder.register(function, alias);
            } else {
                holder.register(function);
            }
        }));
    }

}
