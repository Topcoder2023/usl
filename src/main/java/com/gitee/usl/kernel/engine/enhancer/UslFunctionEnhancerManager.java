package com.gitee.usl.kernel.engine.enhancer;

import com.gitee.usl.api.UslInitializer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.engine.UslAviatorProxyFunction;
import com.gitee.usl.kernel.engine.UslFunction;
import com.gitee.usl.kernel.engine.UslFunctionDefinition;
import com.gitee.usl.kernel.engine.UslFunctionEnhancer;

import java.util.List;
import java.util.Optional;

/**
 * 函数增强器管理者
 * 由于是对已注册的函数进行增强
 * 所以优先级在函数提供者管理器之后
 *
 * @author hongda.li
 */
@Order(UslFunctionEnhancerManager.USL_FUNC_ENHANCER_ORDER)
public class UslFunctionEnhancerManager implements UslInitializer {
    /**
     * USL 函数增强器的优先级
     */
    public static final int USL_FUNC_ENHANCER_ORDER = Integer.MAX_VALUE - 10;

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        List<UslFunctionEnhancer> enhancers = SpiServiceUtil.services(UslFunctionEnhancer.class);

        uslConfiguration.getEngineConfiguration()
                .getFunctionHolder()
                .onVisit(function -> {
                    UslFunctionDefinition definition;

                    // 尝试从AviatorFunction实例中读取函数定义信息
                    if (function instanceof UslFunction uf) {
                        definition = uf.getDefinition();
                    } else if (function instanceof UslAviatorProxyFunction up) {
                        definition = up.getDefinition();
                    } else {
                        definition = null;
                    }

                    // 若成功读取函数定义信息，则进行函数增强
                    Optional.ofNullable(definition)
                            .ifPresent(def -> enhancers.forEach(enhancer -> enhancer.enhance(def)));
                });
    }
}
