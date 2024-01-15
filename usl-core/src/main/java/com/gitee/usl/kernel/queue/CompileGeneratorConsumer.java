package com.gitee.usl.kernel.queue;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.CompileConsumer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.grammar.script.ES;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLCompileException;
import com.google.auto.service.AutoService;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.script.BS;
import com.gitee.usl.grammar.code.CodeGenerator;
import com.gitee.usl.grammar.lexer.ExpressionLexer;
import com.gitee.usl.grammar.parser.ExpressionParser;
import lombok.extern.slf4j.Slf4j;

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
        ScriptEngine instance = event.getConfiguration()
                .getEngineConfig()
                .getEngineInitializer()
                .getInstance();

        if (CharSequenceUtil.isBlank(event.getContent())) {
            event.setExpression(ES.empty().setException(new USLCompileException(ResultCode.SCRIPT_EMPTY)));
            log.warn("脚本内容为空");
            return;
        }

        try {
            log.debug("开始编译脚本 - {}", event.getEventId());
            ExpressionLexer lexer = new ExpressionLexer(instance, event.getContent());
            CodeGenerator codeGenerator = instance.codeGenerator();
            BS expression = (BS) new ExpressionParser(instance, lexer, codeGenerator).parse();
            event.setExpression(expression);
        } catch (USLCompileException uce) {
            log.error("脚本编译失败", uce);
            event.setExpression(ES.empty().setException(uce));
        } catch (Exception e) {
            log.error("脚本编译失败", e);
            event.setExpression(ES.empty().setException(new USLCompileException(e)));
        }
    }

}
