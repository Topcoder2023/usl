package com.gitee.usl.kernel.queue;

import com.gitee.usl.api.CompileConsumer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.ScriptCompileHelper;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.lexer.ExpressionLexer;
import com.googlecode.aviator.parser.ExpressionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * USL 字节码生成器 消费者
 *
 * @author hongda.li
 */
@Order(CompileGeneratorConsumer.GENERATOR_ORDER)
@AutoService(CompileConsumer.class)
public class CompileGeneratorConsumer implements CompileConsumer {

    /**
     * 编译字节码生成器消费者的顺序
     */
    public static final int GENERATOR_ORDER = Integer.MAX_VALUE - 1000;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onEvent(CompileEvent event, long sequence, boolean endOfBatch) {
        EngineConfiguration configuration = event.getConfiguration().configEngine();
        AviatorEvaluatorInstance instance = configuration.scriptEngineManager().getInstance();

        try {
            logger.debug("开始编译脚本 - {}", event.getEventId());
            event.setExpression(new ExpressionParser(instance,
                    new ExpressionLexer(instance, event.getContent()),
                    instance.newCodeGenerator(null, true)).parse());
        } catch (Exception e) {
            logger.error("脚本编译失败", e);
            event.setExpression(ScriptCompileHelper.empty());
        }
    }
}
