package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.CompileConsumer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.BaseExpression;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.code.CodeGenerator;
import com.googlecode.aviator.lexer.ExpressionLexer;
import com.googlecode.aviator.parser.ExpressionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * USL 字节码生成器 消费者
 *
 * @author hongda.li
 */
@Order(CompileGeneratorConsumer.GENERATOR_ORDER)
@AutoService(CompileConsumer.class)
public class CompileGeneratorConsumer implements CompileConsumer {

    /**
     * 空的表达式占位对象
     * 用以替代 null 的 Expression
     */
    private static final Expression EMPTY_PLACE_HOLDER = new BaseExpression(null, null, null) {
        private static final long serialVersionUID = -3628284719346231510L;

        @Override
        public Object executeDirectly(Map<String, Object> env) {
            return null;
        }
    };

    /**
     * 编译字节码生成器消费者的顺序
     */
    public static final int GENERATOR_ORDER = Integer.MAX_VALUE - 1000;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onEvent(CompileEvent event, long sequence, boolean endOfBatch) throws Exception {
        EngineConfiguration configuration = event.getConfiguration().configEngine();
        AviatorEvaluatorInstance instance = configuration.scriptEngineManager().getInstance();

        @SuppressWarnings("ReassignedVariable") Expression expression;

        try {
            expression = this.compileExpression(event, instance);
        } catch (Exception compileError) {
            expression = EMPTY_PLACE_HOLDER;
            logger.error("Compile expression failed.", compileError);
        }

        event.setExpression(expression);
    }

    private Expression compileExpression(CompileEvent event, AviatorEvaluatorInstance instance) {
        // 词法分析
        ExpressionLexer lexer = new ExpressionLexer(instance, event.getContent());
        // 字节码生成器
        CodeGenerator codeGenerator = instance.newCodeGenerator(null, true);
        // 语法分析
        return new ExpressionParser(instance, lexer, codeGenerator).parse();
    }

    public static boolean isInvalid(Expression expression) {
        return EMPTY_PLACE_HOLDER.equals(expression);
    }
}
