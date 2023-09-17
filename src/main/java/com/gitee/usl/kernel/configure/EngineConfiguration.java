package com.gitee.usl.kernel.configure;

import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.engine.UslScriptEngine;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.util.*;

/**
 * @author hongda.li
 */
public class EngineConfiguration {
    private UslScriptEngine scriptEngine;
    private final Set<String> packageNameList;
    private final Map<String, AviatorFunction> functionMap;

    public EngineConfiguration() {
        this.packageNameList = HashSet.newHashSet(NumberConstant.COMMON_SIZE);
        this.functionMap = HashMap.newHashMap(NumberConstant.COMMON_SIZE);
    }

    public Set<String> getPackageNameList() {
        return packageNameList;
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
        List<String> nameList = scanner.stream().map(ClassUtil::getPackage).toList();
        this.packageNameList.addAll(nameList);
        return this;
    }

    public UslScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public EngineConfiguration setScriptEngine(UslScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
        return this;
    }

    public Map<String, AviatorFunction> getFunctionMap() {
        return functionMap;
    }
}
