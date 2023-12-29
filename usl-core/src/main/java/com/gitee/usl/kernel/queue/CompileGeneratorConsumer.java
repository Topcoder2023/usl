package com.gitee.usl.kernel.queue;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.CompileConsumer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.ScriptCompileHelper;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.BaseExpression;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.code.CodeGenerator;
import com.googlecode.aviator.lexer.ExpressionLexer;
import com.googlecode.aviator.lexer.SymbolTable;
import com.googlecode.aviator.lexer.token.Variable;
import com.googlecode.aviator.parser.ExpressionParser;
import com.googlecode.aviator.runtime.LambdaFunctionBootstrap;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author hongda.li
 */
@Slf4j
@Description("USL字节码生成器消费者")
@AutoService(CompileConsumer.class)
@Order(CompileGeneratorConsumer.GENERATOR_ORDER)
public class CompileGeneratorConsumer implements CompileConsumer {

    @Description("编译字节码生成器消费者的顺序")
    public static final int GENERATOR_ORDER = Integer.MAX_VALUE - 1000;

    @Override
    public void onEvent(CompileEvent event, long sequence, boolean endOfBatch) {

        @Description("脚本引擎实例")
        AviatorEvaluatorInstance instance = event.getConfiguration()
                .getEngineConfig()
                .getEngineInitializer()
                .getInstance();

        if (CharSequenceUtil.isBlank(event.getContent())) {
            event.setExpression(ScriptCompileHelper.empty());
            log.warn("脚本内容为空");
            return;
        }

        try {
            log.debug("开始编译脚本 - {}", event.getEventId());
            ExpressionLexer lexer = new ExpressionLexer(instance, event.getContent());
            CodeGenerator codeGenerator = instance.newCodeGenerator(null, true);
            BaseExpression expression = (BaseExpression) new ExpressionParser(instance, lexer, codeGenerator).parse();
            event.setExpression(expression);

            @Description("变量表")
            Map<String, Variable> variableMap = expression.getSymbolTable().getTable();
            System.out.println(variableMap);

        } catch (Exception e) {
            log.error("脚本编译失败", e);
            event.setExpression(ScriptCompileHelper.empty());
        }
    }
}
