package com.gitee.usl.kernel.engine.provider;

import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.gitee.usl.kernel.engine.UslFunctionDefinition;
import com.gitee.usl.kernel.engine.UslFunctionProvider;

import java.util.List;

/**
 * @author hongda.li
 */
public abstract class AbstractUslFunctionProvider implements UslFunctionProvider {

    @Override
    public List<UslFunctionDefinition> provide(EngineConfiguration configuration) {
        return configuration.getPackageNameList()
                .stream()
                .flatMap(packageName -> ClassUtil.scanPackage(packageName, this::filter)
                        .stream()
                        .flatMap(clz -> this.class2Definition(clz)
                                .stream())
                        .toList()
                        .stream())
                .toList();
    }

    /**
     * 将指定类转为 USL 函数定义信息集合
     *
     * @param clz 符合条件的类
     * @return USL 函数定义信息集合
     */
    protected abstract List<UslFunctionDefinition> class2Definition(Class<?> clz);

    /**
     * 过滤出声明式函数类
     * 即类上必须有 @Func 注解
     *
     * @param clz 待过滤的类
     * @return 是否符合声明式函数的格式
     */
    protected abstract boolean filter(Class<?> clz);
}
