package com.gitee.usl.grammar;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.ExceptionHandler;
import com.gitee.usl.api.FunctionMissing;
import com.gitee.usl.api.ScriptProcessor;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.runtime.function.system.*;
import com.gitee.usl.infra.exception.USLException;
import com.gitee.usl.infra.structure.StringMap;
import com.gitee.usl.grammar.Options.Value;
import com.gitee.usl.grammar.lexer.token.OperatorType;
import com.gitee.usl.grammar.runtime.RuntimeFunctionDelegator;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.utils.CommonUtils;
import com.gitee.usl.kernel.engine.USLConfiguration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;

/**
 * SHA256 —— 唯一缓存键生成,
 * SpinLock —— 异步加载编译结果,
 * W-TinyLFU —— 缓存淘汰算法,
 * Token-Bucket —— 令牌桶限流组件,
 * Timing-Wheel —— 时间轮任务调度,
 * Count–Min Sketch —— 编译缓存计数算法,
 * Non-blocking Synchronization —— 无锁化编程
 *
 * @author hongda.li
 */
@Slf4j
@Getter
@Description("脚本引擎实例")
public final class ScriptEngine {

    @Setter
    @Description("前置后置处理器")
    private ScriptProcessor envProcessor;

    @Description("函数兜底机制")
    private final FunctionMissing functionMissing;

    @Description("函数映射")
    private final Function<String, _Function> functionMapping;

    @Description("脚本引擎选项配置集合")
    private volatile Map<Options, Value> options = new IdentityHashMap<>();

    @Description("系统函数表")
    private final StringMap<_Function> systemFunctionMap = new StringMap<>();

    @Getter
    @Description("自定义异常处理器")
    private final ExceptionHandler exceptionHandler;

    @Description("操作符别名映射关系")
    private final Map<OperatorType, String> aliasOperatorTokens = new IdentityHashMap<>();

    @Description("操作符函数表")
    private final Map<OperatorType, _Function> operatorFunctionMap = new IdentityHashMap<>();

    @Description("脚本引擎实例构造器")
    public ScriptEngine(USLConfiguration configuration) {
        this.functionMapping = name -> configuration.getFunctionHolder().search(name);
        this.functionMissing = configuration.getFunctionMissing();
        this.exceptionHandler = configuration.getExceptionHandler();
        for (Options opt : Options.values()) {
            this.options.put(opt, opt.getDefaultValueObject());
        }
        setOption(Options.EVAL_MODE, EvalMode.ASM);
        loadFeatureFunctions();
        loadSystemFunctions();
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
                    for (_Function fn : feat.getFunctions()) {
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
        addSystemFunction(Singleton.get(WithMetaFunction.class));
    }

    @Description("添加特性绑定函数")
    private void loadFeatureFunctions() {
        this.options.get(Options.FEATURE_SET).featureSet
                .stream()
                .map(Feature::getFunctions)
                .flatMap(Collection::stream)
                .filter(fn -> !existsSystemFunction(fn.name()))
                .forEach(this::addSystemFunction);
    }

    @Description("添加内置函数")
    public void addSystemFunction(final _Function function) {
        Assert.notNull(function, "函数实例不能为空");

        String name = function.name();

        if (ScriptKeyword.isReservedKeyword(name)) {
            throw new USLException("函数名称与保留关键字冲突 - {}", name);
        }

        if (this.systemFunctionMap.containsKey(name)) {
            throw new USLException("已存在相同名称的函数 - {}", name);
        }

        this.systemFunctionMap.put(name, function);
        log.debug("注册系统内置函数 - [{}]", name);
    }

    @Description("尝试获取函数")
    public _Function getFunction(final String name) {
        return this.getFunction(name, null);
    }

    @Description("尝试获取函数")
    public _Function getFunction(final String name, final ScriptKeyword symbolTable) {
        Assert.notNull(functionMapping, "函数加载器尚未初始化");

        @Description("从内置函数中尝试获取函数")
        _Function function = this.systemFunctionMap.get(name);
        if (function != null) {
            return function;
        }

        @Description("从函数映射中尝试获取函数")
        _Function fromLoader = functionMapping.apply(name);
        if (fromLoader != null) {
            return fromLoader;
        }

        @Description("从上下文中尝试获取函数并转为委托函数")
        _Function fromEnv = new RuntimeFunctionDelegator(name, symbolTable, this.functionMissing);
        return fromEnv;
    }

    @Description("重载操作符函数")
    public void addOpFunction(final OperatorType opType, final _Function function) {
        this.operatorFunctionMap.put(opType, function);
    }

    @Description("获取操作符函数实现")
    public _Function getOpFunction(final OperatorType opType) {
        return this.operatorFunctionMap.get(opType);
    }

    @Description("移除操作符函数")
    public _Function removeOpFunction(final OperatorType opType) {
        return this.operatorFunctionMap.remove(opType);
    }

    @Description("判断系统内置函数是否存在")
    public boolean existsSystemFunction(final String name) {
        return this.systemFunctionMap.containsKey(name);
    }

    @Description("移除系统内置函数")
    public void removeSystemFunction(final _Function function) {
        Assert.notNull(function, "函数实例不能为空");
        this.systemFunctionMap.remove(function.name());
    }

}
