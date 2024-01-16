package com.gitee.usl.grammar.script;

import com.gitee.usl.grammar.ExceptionHandler;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.ScriptKeyword;
import com.gitee.usl.grammar.parser.IR;
import com.gitee.usl.grammar.parser.InterpretContext;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;

import java.util.*;

/**
 * @author hongda.li
 */
public class IE extends BS {

    private final boolean unbox;

    private final List<IR> instruments;

    public IE(final ScriptEngine instance,
              final ScriptKeyword symbolTable,
              final List<IR> instruments,
              final boolean unbox) {
        super(instance, symbolTable);
        this.instruments = instruments;
        this.unbox = unbox;
    }

    @Override
    public Object defaultImpl(Map<String, Object> env) {
        InterpretContext ctx = new InterpretContext(this, this.instruments, (Env) env);
        ctx.dispatch(false);

        AviatorObject result;
        try {
            result = ctx.peek();
        } catch (Exception t) {
            Optional.ofNullable(getInstance().getExceptionHandler())
                    .orElse(ExceptionHandler.DEFAULT)
                    .accept(t);
            return null;
        }

        return Optional.ofNullable(result)
                .map(res -> res.self(env, unbox))
                .orElse(null);
    }

}
