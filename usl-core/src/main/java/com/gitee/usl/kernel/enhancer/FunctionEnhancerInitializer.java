package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.api.FunctionEnhancer;
import com.google.auto.service.AutoService;

import java.util.List;

/**
 * @author hongda.li
 */
@Description("函数增强初始化器")
@Order(FunctionEnhancerInitializer.USL_FUNC_ENHANCER_ORDER)
@AutoService(Initializer.class)
public class FunctionEnhancerInitializer implements Initializer {

    @Description("函数增强初始化器的优先级")
    public static final int USL_FUNC_ENHANCER_ORDER = Integer.MAX_VALUE - 10;

    @Override
    public void doInit(Configuration configuration) {

        @Description("函数增强器集合")
        List<FunctionEnhancer> enhancers = ServiceSearcher.searchAll(FunctionEnhancer.class);

        configuration.getEngineConfig()
                .getFunctionHolder()
                .onVisit(function -> enhancers.forEach(enhancer -> enhancer.enhance(function)));
    }

}
