package com.gitee.usl.kernel.queue;

import cn.hutool.core.collection.CollUtil;
import com.gitee.usl.api.CompileConsumer;
import com.gitee.usl.api.VariableInitializer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.grammar.asm.ES;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.infra.structure.StringMap;
import com.gitee.usl.kernel.configure.EngineConfig;
import com.google.auto.service.AutoService;
import com.gitee.usl.grammar.asm.BS;
import com.googlecode.aviator.lexer.token.Variable;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

/**
 * @author hongda.li
 */
@Slf4j
@AutoService(CompileConsumer.class)
@Order(CompileGeneratorConsumer.GENERATOR_ORDER + 50)
public class CompileVariableConsumer implements CompileConsumer {
    @Override
    public void onEvent(CompileEvent event, long sequence, boolean endOfBatch) {
        BS expression = (BS) event.getExpression();

        if (expression == null || expression instanceof ES) {
            return;
        }

        @Description("变量表")
        Map<String, Variable> variableMap = expression.getSymbolTable().getTable();
        if (CollUtil.isEmpty(variableMap)) {
            return;
        }

        @Description("引擎配置")
        EngineConfig engineConfig = event.getConfiguration().getEngineConfig();

        @Description("变量初始化器")
        VariableInitializer initializer = engineConfig.getVarInitializer();
        if (initializer == null) {
            return;
        }

        @Description("函数容器")
        FunctionHolder functionHolder = engineConfig.getFunctionHolder();

        @Description("初始化结果")
        StringMap<Object> initEnv = new StringMap<>(variableMap.size());

        variableMap.forEach((name, var) -> {
            if (functionHolder.search(name) == null) {
                Optional.ofNullable(initializer.doInit(var)).ifPresent(result -> {
                    initEnv.put(name, result);
                    log.debug("初始化变量 - [{} : {}]", name, result);
                });
            }
        });

        event.setInitEnv(initEnv);
    }
}
