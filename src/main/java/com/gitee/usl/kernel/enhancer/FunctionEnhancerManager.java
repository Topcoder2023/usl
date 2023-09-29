package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.UslInitializer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.engine.UslFunctionEnhancer;
import com.google.auto.service.AutoService;

import java.util.List;

/**
 * 函数增强器管理者
 * 由于是对已注册的函数进行增强
 * 所以优先级在函数提供者管理器之后
 *
 * @author hongda.li
 */
@Order(FunctionEnhancerManager.USL_FUNC_ENHANCER_ORDER)
@AutoService(UslInitializer.class)
public class FunctionEnhancerManager implements UslInitializer {
    /**
     * USL 函数增强器的优先级
     */
    public static final int USL_FUNC_ENHANCER_ORDER = Integer.MAX_VALUE - 10;

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        List<UslFunctionEnhancer> enhancers = SpiServiceUtil.services(UslFunctionEnhancer.class);

        uslConfiguration.getEngineConfiguration()
                .getFunctionHolder()
                .onVisit(function -> enhancers.forEach(enhancer -> enhancer.enhance(function)));
    }
}