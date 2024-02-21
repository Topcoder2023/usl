package com.gitee.usl.grammar;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.ExceptionHandler;
import com.gitee.usl.api.FunctionMissing;
import com.gitee.usl.api.ScriptProcessor;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.exception.USLException;
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
        this.options = newOpts;
    }

    @Description("获取脚本引擎配置选项")
    public Value getOptionValue(final Options opt) {
        return this.options.get(opt);
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

    @Description("尝试获取函数")
    public _Function getFunction(final String name) {
        return this.getFunction(name, null);
    }

    @Description("尝试获取函数")
    public _Function getFunction(final String name, final ScriptKeyword symbolTable) {
        return Optional.ofNullable(functionMapping.apply(name)).orElse(new RuntimeFunctionDelegator(name, symbolTable, this.functionMissing));
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

}
