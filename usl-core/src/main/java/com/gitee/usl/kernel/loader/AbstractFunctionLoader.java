package com.gitee.usl.kernel.loader;

import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.api.FunctionLoader;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.kernel.engine.USLConfiguration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
public abstract class AbstractFunctionLoader implements FunctionLoader {

    @Override
    public List<_Function> load(USLConfiguration configuration) {
        USLRunner runner = configuration.getRunner();

        return configuration.getPackageNameList()
                .stream()
                .flatMap(packageName -> ClassUtil.scanPackage(packageName, clz -> this.filter(clz, configuration))
                        .stream()
                        .flatMap(clz -> this.class2Definition(clz, runner).stream())
                        .toList()
                        .stream())
                .toList()
                .stream()
                .map(this::definition2Func)
                .collect(Collectors.toList());
    }

    @Description("过滤出声明式函数类")
    protected abstract boolean filter(Class<?> clz, USLConfiguration configuration);

    @Description("将函数定义信息转为函数实例")
    protected abstract _Function definition2Func(FunctionDefinition definition);

    @Description("将指定类转为USL函数定义信息集合")
    protected abstract List<FunctionDefinition> class2Definition(Class<?> clz, USLRunner runner);

}
