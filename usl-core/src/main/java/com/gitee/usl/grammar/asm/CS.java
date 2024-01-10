package com.gitee.usl.grammar.asm;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.AsmMethod;
import com.gitee.usl.grammar.ExceptionHandler;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.ScriptKeyword;
import com.googlecode.aviator.parser.VariableMeta;
import com.googlecode.aviator.utils.Env;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author hongda.li
 */
@Description("基于类的表达式")
public abstract class CS extends BS {

    public CS(final ScriptEngine instance,
              final List<VariableMeta> vars,
              final ScriptKeyword symbolTable) {
        super(instance, vars, symbolTable);
    }

    @AsmMethod
    @Description("自定义实现")
    public abstract Object customImpl(Env env);

    @Override
    public Object defaultImpl(final Map<String, Object> env) {
        try {
            return this.customImpl((Env) env);
        } catch (Throwable t) {
            Optional.ofNullable(getInstance().getExceptionHandler())
                    .orElse(ExceptionHandler.DEFAULT)
                    .accept(t);
            return null;
        }
    }

}
