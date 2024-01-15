package com.gitee.usl.grammar.script;

import java.util.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.AsmConstants;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLCompileException;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.ScriptProcessor;
import com.gitee.usl.grammar.ScriptKeyword;
import com.gitee.usl.grammar.parser.VariableMeta;
import com.gitee.usl.grammar.runtime.FunctionArgument;
import com.gitee.usl.grammar.runtime.LambdaFunctionBootstrap;
import com.gitee.usl.grammar.runtime.function.LambdaFunction;
import com.gitee.usl.grammar.utils.Env;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hongda.li
 */
@Getter
@Description("基础脚本")
public abstract class BS implements Script, ScriptEnhancer {

    @Description("脚本引擎实例")
    protected final ScriptEngine instance;

    @Description("变量表")
    protected final ScriptKeyword symbolTable;

    @Setter
    @Description("Lambda函数组")
    protected Map<String, LambdaFunctionBootstrap> lambdaBootstraps;

    @Description("上下文前后置处理器")
    private final ScriptProcessor envProcessor;

    @Description("上下文环境")
    private Env compileEnv;

    @Description("函数参数表")
    private List<FunctionArgument> functionsArgs = Collections.emptyList();

    public BS(final ScriptEngine instance,
              final ScriptKeyword symbolTable) {
        this.instance = instance;
        this.symbolTable = symbolTable;
        this.envProcessor = Optional.ofNullable(this.instance).map(ScriptEngine::getEnvProcessor).orElse(ScriptProcessor.EMPTY);
    }

    @Description("函数逻辑的默认实现")
    public abstract Object defaultImpl(final Map<String, Object> env);

    @Override
    public Object execute(Map<String, Object> context) {
        this.buildArgumentList();
        final Env env = this.buildEnv(context);
        try {
            envProcessor.onBegin(env, this);
            return this.defaultImpl(env);
        } finally {
            envProcessor.onAfter(env, this);
        }
    }

    @Override
    public void setFunctionsArgs(final List<FunctionArgument> functionsArgs) {
        Optional.ofNullable(functionsArgs).ifPresent(args -> this.functionsArgs = args);
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
    public LambdaFunction newLambda(final Env env, final String name) {
        LambdaFunctionBootstrap bootstrap = this.lambdaBootstraps.get(name);
        Assert.notNull(bootstrap, () -> new USLCompileException(ResultCode.NOT_FOUND_OF_LAMBDA, name));
        return bootstrap.newInstance(env);
    }

    @Description("合并所有参数列表")
    private void buildArgumentList() {
        if (CollUtil.isEmpty(lambdaBootstraps)) {
            return;
        }
        lambdaBootstraps.values().forEach(lambdaFunctionBootstrap -> {
            Script expression = lambdaFunctionBootstrap.getExpression();
            if (!(expression instanceof IE)) {
                return;
            }
            List<FunctionArgument> arguments = ((IE) expression).getFunctionsArgs();
            if (CollUtil.isEmpty(arguments)) {
                return;
            }
            this.functionsArgs.addAll(arguments);
        });
    }

}
