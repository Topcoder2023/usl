package com.gitee.usl.grammar.asm;

import java.util.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.annotation.AsmMethod;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.EmptyEnvProcessor;
import com.gitee.usl.infra.constant.AsmConstants;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLCompileException;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.EnvProcessor;
import com.googlecode.aviator.lexer.SymbolTable;
import com.googlecode.aviator.parser.VariableMeta;
import com.googlecode.aviator.runtime.FunctionArgument;
import com.googlecode.aviator.runtime.LambdaFunctionBootstrap;
import com.googlecode.aviator.runtime.function.LambdaFunction;
import com.googlecode.aviator.utils.Env;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hongda.li
 */
@Getter
@Description("基础脚本")
public abstract class BS implements Script, ScriptEnhancer {

    @Description("变量表")
    private final SymbolTable symbolTable;

    @Description("上下文前后置处理器")
    private final EnvProcessor envProcessor;

    @Description("变量属性表")
    private final List<VariableMeta> metaList;

    @Description("脚本引擎实例")
    private final AviatorEvaluatorInstance instance;

    @Description("上下文环境")
    private Env compileEnv;

    @Setter
    @Description("Lambda函数组")
    private Map<String, LambdaFunctionBootstrap> lambdaBootstraps;

    @Description("函数参数表")
    private Map<Integer, List<FunctionArgument>> functionsArgs = Collections.emptyMap();

    public BS(final AviatorEvaluatorInstance instance,
              final List<VariableMeta> metaList,
              final SymbolTable symbolTable) {
        this.metaList = metaList;
        this.instance = instance;
        this.symbolTable = symbolTable;
        this.envProcessor = Optional.ofNullable(this.instance)
                .map(AviatorEvaluatorInstance::getEnvProcessor)
                .orElse(EmptyEnvProcessor.getInstance());
    }

    @Description("函数逻辑的默认实现")
    public abstract Object defaultImpl(final Map<String, Object> env);

    @Override
    public Object execute(Map<String, Object> context) {
        final Env env = this.buildEnv(context);
        envProcessor.beforeExecute(env, this);
        try {
            return this.defaultImpl(env);
        } finally {
            envProcessor.afterExecute(env, this);
        }
    }

    @Override
    public void setFunctionsArgs(final Map<Integer, List<FunctionArgument>> functionsArgs) {
        Optional.ofNullable(functionsArgs)
                .map(Collections::unmodifiableMap)
                .ifPresent(args -> this.functionsArgs = args);
    }

    @Override
    public void setCompileEnv(final Env compileEnv) {
        this.compileEnv = compileEnv;
        this.compileEnv.setExpression(this);
    }

    @Override
    public Object execute() {
        return this.execute(null);
    }

    @Description("构建上下文环境")
    private Env buildEnv(final Map<String, Object> map) {
        if (map instanceof Env) {
            ((Env) map).configure(this.instance, this);
        }
        Env env = new Env(map, CollUtil.isEmpty(map) ? new HashMap<>() : map);
        env.configure(this.instance, this);
        if (this.compileEnv != null && !this.compileEnv.isEmpty()) {
            env.putAll(this.compileEnv);
        }
        if (!this.functionsArgs.isEmpty()) {
            env.override(AsmConstants.FUNC_PARAMS_VAR, this.functionsArgs);
        }
        return env;
    }

    @Override
    @AsmMethod
    public LambdaFunction newLambda(final Env env, final String name) {
        LambdaFunctionBootstrap bootstrap = this.lambdaBootstraps.get(name);
        Assert.notNull(bootstrap, () -> new USLCompileException(ResultCode.NOT_FOUND_OF_LAMBDA, name));
        return bootstrap.newInstance(env);
    }

}
