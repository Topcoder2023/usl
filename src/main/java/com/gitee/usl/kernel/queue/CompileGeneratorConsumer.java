package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.code.CodeGenerator;
import com.googlecode.aviator.lexer.ExpressionLexer;
import com.googlecode.aviator.parser.ExpressionParser;

/**
 * USL 字节码生成器 消费者
 *
 * @author hongda.li
 */
@Order(CompileGeneratorConsumer.GENERATOR_ORDER)
public class CompileGeneratorConsumer implements CompileConsumer {
    /**
     * 编译字节码生成器消费者的顺序
     */
    public static final int GENERATOR_ORDER = Integer.MAX_VALUE - 100;

    @Override
    public void onEvent(CompileEvent event, long sequence, boolean endOfBatch) throws Exception {
        EngineConfiguration configuration = event.getConfiguration().getEngineConfiguration();
        AviatorEvaluatorInstance instance = configuration.getInstance();

        // 词法分析
        ExpressionLexer lexer = new ExpressionLexer(instance, event.getContent());
        // 字节码生成器
        CodeGenerator codeGenerator = instance.newCodeGenerator(null, event.isCached());
        // 语法分析
        Expression expression = new ExpressionParser(instance, lexer, codeGenerator).parse();

        event.setExpression(expression);
    }
}
