package com.gitee.usl.kernel.configure;

import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.kernel.engine.ScriptEngineManager;

import java.util.*;

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
        this.packageNameList = HashSet.newHashSet(NumberConstant.COMMON_SIZE);
        this.functionHolder = new FunctionHolder();
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
        List<String> nameList = scanner.stream().map(ClassUtil::getPackage).toList();
        this.packageNameList.addAll(nameList);
        return this;
    }

    public ScriptEngineManager getScriptEngineManager() {
        return scriptEngineManager;
    }

    public EngineConfiguration setScriptEngineManager(ScriptEngineManager scriptEngineManager) {
        this.scriptEngineManager = scriptEngineManager;
        return this;
    }

    public FunctionHolder getFunctionHolder() {
        return functionHolder;
    }
}