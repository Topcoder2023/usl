package com.gitee.usl.kernel.configure;

import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.kernel.engine.ScriptEngineManager;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
public class EngineConfiguration {
    private final Configuration configuration;
    private final Set<String> packageNameList;
    private final FunctionHolder functionHolder;
    private ScriptEngineManager scriptEngineManager;

    public EngineConfiguration(Configuration configuration) {
        this.configuration = configuration;
        this.functionHolder = new FunctionHolder();
        this.packageNameList = new HashSet<>(NumberConstant.COMMON_SIZE);
    }

    public Set<String> getPackageNameList() {
        return packageNameList;
    }

    public Configuration finish() {
        return this.configuration;
    }

    public EngineConfiguration scan(Class<?> root) {
        this.packageNameList.add(ClassUtil.getPackage(root));
        return this;
    }

    public EngineConfiguration scan(String packageName) {
        this.packageNameList.add(packageName);
        return this;
    }

    public EngineConfiguration scan(Class<?>... scanner) {
        return scan(new HashSet<>(Arrays.asList(scanner)));
    }

    public EngineConfiguration scan(String... packageNameList) {
        this.packageNameList.addAll(Arrays.asList(packageNameList));
        return this;
    }

    public EngineConfiguration scan(Set<Class<?>> scanner) {
        List<String> nameList = scanner.stream()
                .map(ClassUtil::getPackage)
                .collect(Collectors.toList());
        this.packageNameList.addAll(nameList);
        return this;
    }

    public EngineConfiguration filter(Predicate<AviatorFunction> filter) {
        this.functionHolder.addFilter(filter);
        return this;
    }

    public ScriptEngineManager scriptEngineManager() {
        return scriptEngineManager;
    }

    public EngineConfiguration setScriptEngineManager(ScriptEngineManager scriptEngineManager) {
        this.scriptEngineManager = scriptEngineManager;
        return this;
    }

    public FunctionHolder functionHolder() {
        return functionHolder;
    }
}
