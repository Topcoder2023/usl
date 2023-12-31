package com.googlecode.aviator;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.exception.USLException;
import com.googlecode.aviator.Options.Value;
import com.googlecode.aviator.asm.Opcodes;
import com.googlecode.aviator.code.CodeGenerator;
import com.googlecode.aviator.code.OptimizeCodeGenerator;
import com.googlecode.aviator.lexer.SymbolTable;
import com.googlecode.aviator.lexer.token.OperatorType;
import com.googlecode.aviator.parser.AviatorClassLoader;
import com.googlecode.aviator.runtime.RuntimeFunctionDelegator;
import com.googlecode.aviator.runtime.function.system.*;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * @author hongda.li
 */
@Slf4j
@Getter
@Description("脚本引擎实例")
public final class AviatorEvaluatorInstance {

    @Setter
    @Description("前置后置处理器")
    private EnvProcessor envProcessor;

    @Setter
    @Description("函数加载器")
    private FunctionLoader functionLoader;

    @Setter
    @Description("函数兜底机制")
    private FunctionMissing functionMissing;

    @Setter
    @Description("字节码版本号")
    private int bytecodeVersion = Opcodes.V1_7;

    @Description("自定义类加载器")
    private final AviatorClassLoader classLoader;

    @Description("系统函数表")
    private final Map<String, Object> systemFunctionMap = new HashMap<>();

    @Description("脚本引擎选项配置集合")
    private volatile Map<Options, Value> options = new IdentityHashMap<>();

    @Description("操作符别名映射关系")
    private final Map<OperatorType, String> aliasOperatorTokens = new IdentityHashMap<>();

    @Description("操作符函数表")
    private final Map<OperatorType, AviatorFunction> operatorFunctionMap = new IdentityHashMap<>();

    @Description("脚本引擎实例构造器")
    public AviatorEvaluatorInstance() {
        for (Options opt : Options.values()) {
            this.options.put(opt, opt.getDefaultValueObject());
        }
        setOption(Options.EVAL_MODE, EvalMode.ASM);
        loadFeatureFunctions();
        loadSystemFunctions();
        this.classLoader = AccessController.doPrivileged((PrivilegedAction<AviatorClassLoader>) () -> new AviatorClassLoader(this.getClass().getClassLoader()));
    }

    @Description("为操作符函数建立别名")
    public void aliasOperator(final OperatorType type, final String token) {
        CommonUtils.checkJavaIdentifier(token);
        if ((type != OperatorType.AND && type != OperatorType.OR)) {
            throw new USLException("暂不支持的Token类型 - {}", type);
        }
        this.aliasOperatorTokens.put(type, token);
    }

    @Description("获取指定操作符的别名")
    public String getOperatorAliasToken(final OperatorType type) {
        return this.aliasOperatorTokens.get(type);
    }

    @Description("设置脚本引擎配置选项")
    public void setOption(final Options opt, final Object val) {
        if (opt == null || val == null) {
            throw new IllegalArgumentException("Option and value should not be null.");
        }
        if (!opt.isValidValue(val)) {
            throw new IllegalArgumentException("Invalid value for option:" + opt.name());
        }
        Map<Options, Value> newOpts = new IdentityHashMap<>(this.options);
        newOpts.put(opt, opt.intoValue(val));
        if (opt == Options.FEATURE_SET) {
            Set<Feature> oldSet = new HashSet<>(getFeatures());
            @SuppressWarnings("unchecked")
            Set<Feature> newSet = (Set<Feature>) val;
            if (oldSet.removeAll(newSet)) {
                // removed functions that feature is disabled.
                for (Feature feat : oldSet) {
                    for (AviatorFunction fn : feat.getFunctions()) {
                        this.removeSystemFunction(fn);
                    }
                }
            }
        }
        this.options = newOpts;
        if (opt == Options.FEATURE_SET) {
            loadFeatureFunctions();
        }
    }

    @Description("获取脚本引擎配置选项")
    public Value getOptionValue(final Options opt) {
        return this.options.get(opt);
    }

    @Description("获取脚本引擎语法特性配置集合")
    public Set<Feature> getFeatures() {
        return this.options.get(Options.FEATURE_SET).featureSet;
    }

    @Description("判断脚本引擎语法特性配置是否启用")
    public boolean isFeatureEnabled(final Feature feature) {
        return this.options.get(Options.FEATURE_SET).featureSet.contains(feature);
    }

    @Description("确保已启用指定特性")
    public void ensureFeatureEnabled(final Feature feature) {
        boolean enabled = getOptionValue(Options.FEATURE_SET).featureSet.contains(feature);
        Assert.isTrue(enabled, () -> new USLException("脚本引擎特性未启用 - [{}]", feature));
    }

    @Description("禁用脚本引擎语法特性配置")
    public void disableFeature(final Feature feature) {
        this.options.get(Options.FEATURE_SET).featureSet.remove(feature);
        for (AviatorFunction fn : feature.getFunctions()) {
            this.removeSystemFunction(fn);
        }
        loadFeatureFunctions();
    }

    @Description("加载系统内置函数表")
    private void loadSystemFunctions() {
        addSystemFunction(new BinaryFunction(OperatorType.ADD));
        addSystemFunction(new BinaryFunction(OperatorType.Exponent));
        addSystemFunction(new BinaryFunction(OperatorType.SUB));
        addSystemFunction(new BinaryFunction(OperatorType.MULT));
        addSystemFunction(new BinaryFunction(OperatorType.DIV));
        addSystemFunction(new BinaryFunction(OperatorType.MOD));
        addSystemFunction(new BinaryFunction(OperatorType.NEG));
        addSystemFunction(new BinaryFunction(OperatorType.NOT));
        addSystemFunction(new BinaryFunction(OperatorType.BIT_AND));
        addSystemFunction(new BinaryFunction(OperatorType.BIT_OR));
        addSystemFunction(new BinaryFunction(OperatorType.BIT_XOR));
        addSystemFunction(new BinaryFunction(OperatorType.BIT_NOT));
        addSystemFunction(IsAFunction.INSTANCE);
        addSystemFunction(MetaFunction.INSTANCE);
        addSystemFunction(WithMetaFunction.INSTANCE);
        addSystemFunction(WithoutMetaFunction.INSTANCE);
    }

    @Description("添加特性绑定函数")
    private void loadFeatureFunctions() {
        this.options.get(Options.FEATURE_SET).featureSet
                .stream()
                .map(Feature::getFunctions)
                .flatMap(Collection::stream)
                .filter(fn -> !existsSystemFunction(fn.getName()))
                .forEach(this::addSystemFunction);
    }

    @Description("添加内置函数")
    public void addSystemFunction(final AviatorFunction function) {
        Assert.notNull(function, "函数实例不能为空");

        String name = function.getName();

        if (SymbolTable.isReservedKeyword(name)) {
            throw new USLException("函数名称与保留关键字冲突 - {}", name);
        }

        if (this.systemFunctionMap.containsKey(name)) {
            throw new USLException("已存在相同名称的函数 - {}", name);
        }

        this.systemFunctionMap.put(name, function);
        log.debug("注册系统内置函数 - [{}]", name);
    }

    @Description("尝试获取函数")
    public AviatorFunction getFunction(final String name) {
        return this.getFunction(name, null);
    }

    @Description("尝试获取函数")
    public AviatorFunction getFunction(final String name, final SymbolTable symbolTable) {
        Assert.notNull(functionLoader, "函数加载器尚未初始化");

        @Description("从内置函数中尝试获取函数")
        AviatorFunction function = (AviatorFunction) this.systemFunctionMap.get(name);
        if (function != null) {
            return function;
        }

        @Description("从函数加载器中尝试获取函数")
        AviatorFunction fromLoader = functionLoader.onFunctionNotFound(name);
        if (fromLoader != null) {
            return fromLoader;
        }

        @Description("从上下文中尝试获取函数并转为委托函数")
        AviatorFunction fromEnv = new RuntimeFunctionDelegator(name, symbolTable, this.functionMissing);
        return fromEnv;
    }

    @Description("重载操作符函数")
    public void addOpFunction(final OperatorType opType, final AviatorFunction function) {
        this.operatorFunctionMap.put(opType, function);
    }

    @Description("获取操作符函数实现")
    public AviatorFunction getOpFunction(final OperatorType opType) {
        return this.operatorFunctionMap.get(opType);
    }

    @Description("移除操作符函数")
    public AviatorFunction removeOpFunction(final OperatorType opType) {
        return this.operatorFunctionMap.remove(opType);
    }

    @Description("判断系统内置函数是否存在")
    public boolean existsSystemFunction(final String name) {
        return this.systemFunctionMap.containsKey(name);
    }

    @Description("移除系统内置函数")
    public void removeSystemFunction(final AviatorFunction function) {
        Assert.notNull(function, "函数实例不能为空");
        this.systemFunctionMap.remove(function.getName());
    }

    @Description("构建一个运行期间优先的字节码生成器")
    public CodeGenerator codeGenerator() {
        return this.codeGenerator(this.classLoader, null);
    }

    @Description("构建一个运行期间优先的字节码生成器")
    public CodeGenerator codeGenerator(final AviatorClassLoader classLoader, final String sourceFile) {
        return new OptimizeCodeGenerator(this, sourceFile, classLoader);
    }

}
