package com.gitee.usl.kernel.loader;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.api.FunctionLoader;
import com.gitee.usl.api.annotation.Import;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.kernel.engine.USLConfiguration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
public class ImportFunctionLoader implements FunctionLoader {

    @Override
    public List<_Function> load(USLConfiguration configuration) {
        return configuration.getPackageNameList()
                .stream()
                .flatMap(packageName -> ClassUtil.scanPackage(packageName, clz -> AnnotationUtil.hasAnnotation(clz, Import.class)).stream())
                .flatMap(clz -> Stream.of(AnnotationUtil.getAnnotation(clz, Import.class).value()))
                .flatMap(clz -> configuration.getLoaders()
                        .stream()
                        .filter(loader -> AbstractFunctionLoader.class.isAssignableFrom(loader.getClass()))
                        .map(loader -> (AbstractFunctionLoader) loader)
                        .filter(loader -> loader.filter(clz, configuration))
                        .flatMap(loader -> loader.class2Definition(clz, configuration.getRunner())
                                .stream()
                                .map(loader::definition2Func)))
                .collect(Collectors.toList());
    }
}
