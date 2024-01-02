package com.gitee.usl.grammar.asm;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.AsmMethod;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.lexer.SymbolTable;
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

    public CS(final AviatorEvaluatorInstance instance,
              final List<VariableMeta> vars,
              final SymbolTable symbolTable) {
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
                    .orElse(error -> {
                        throw new USLExecuteException(error);
                    })
                    .accept(t);
            return null;
        }
    }

}
