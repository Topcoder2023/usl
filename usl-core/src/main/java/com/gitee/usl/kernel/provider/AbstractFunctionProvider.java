package com.gitee.usl.kernel.provider;

import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.configure.EngineConfig;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.api.FunctionProvider;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
public abstract class AbstractFunctionProvider implements FunctionProvider {

    @Override
    public List<AviatorFunction> provide(EngineConfig configuration) {
        USLRunner runner = configuration.getConfiguration().getRunner();

        return configuration.getPackageNameList()
                .stream()
                .flatMap(packageName -> ClassUtil.scanPackage(packageName, this::filter)
                        .stream()
                        .flatMap(clz -> this.class2Definition(clz, runner).stream())
                        .collect(Collectors.toList())
                        .stream())
                .collect(Collectors.toList())
                .stream()
                .map(this::definition2Func)
                .collect(Collectors.toList());
    }

    /**
     * 将指定类转为 USL 函数定义信息集合
     *
     * @param clz    符合条件的类
     * @param runner 当前使用的 USL 实例
     * @return USL 函数定义信息集合
     */
    protected abstract List<FunctionDefinition> class2Definition(Class<?> clz, USLRunner runner);

    /**
     * 将函数定义信息转为函数实例
     *
     * @param definition 函数定义信息
     * @return 函数实例
     */
    protected abstract AviatorFunction definition2Func(FunctionDefinition definition);

    /**
     * 过滤出声明式函数类
     * 即类上必须有 @Func 注解
     *
     * @param clz 待过滤的类
     * @return 是否符合声明式函数的格式
     */
    protected abstract boolean filter(Class<?> clz);
}
