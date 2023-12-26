package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Notes;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.constant.ModuleConstant;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.api.FunctionEnhancer;
import com.google.auto.service.AutoService;

import java.util.List;

/**
 * 函数增强器管理者
 * 由于是对已注册的函数进行增强
 * 所以优先级在函数提供者管理器之后
 *
 * @author hongda.li
 */
@Notes(value = "函数增强初始化器",
        belongs = ModuleConstant.USL_CORE,
        viewUrl = "https://gitee.com/yixi-dlmu/usl/raw/master/usl-core/src/main/java/com/gitee/usl/kernel/enhancer/FunctionEnhancerManager.java")
@Order(FunctionEnhancerManager.USL_FUNC_ENHANCER_ORDER)
@AutoService(Initializer.class)
public class FunctionEnhancerManager implements Initializer {
    /**
     * USL 函数增强器的优先级
     */
    public static final int USL_FUNC_ENHANCER_ORDER = Integer.MAX_VALUE - 10;

    @Override
    public void doInit(Configuration configuration) {
        List<FunctionEnhancer> enhancers = ServiceSearcher.searchAll(FunctionEnhancer.class);

        configuration.getEngineConfig()
                .getFunctionHolder()
                .onVisit(function -> enhancers.forEach(enhancer -> enhancer.enhance(function)));
    }
}
