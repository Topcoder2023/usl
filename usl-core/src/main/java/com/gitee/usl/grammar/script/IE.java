package com.gitee.usl.grammar.script;

import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.ScriptKeyword;
import com.gitee.usl.grammar.code.interpreter.IR;
import com.gitee.usl.grammar.code.interpreter.InterpretContext;
import com.gitee.usl.grammar.code.interpreter.ir.LoadIR;
import com.gitee.usl.grammar.lexer.token.Token;
import com.gitee.usl.grammar.parser.VariableMeta;
import com.gitee.usl.grammar.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;

import java.util.*;

public class IE extends BS {

    private final List<IR> instruments;

    private final boolean unboxObject;

    public IE(final ScriptEngine instance,
              final ScriptKeyword symbolTable,
              final List<IR> instruments,
              final boolean unboxObject) {
        super(instance, symbolTable);
        this.instruments = instruments;
        this.unboxObject = unboxObject;
    }


    @Override
    public Object defaultImpl(Map<String, Object> env) {
        InterpretContext ctx = new InterpretContext(this, this.instruments, (Env) env);
        ctx.dispatch(false);
        AviatorObject result = ctx.peek();
        if (result == null) {
            return null;
        }
        if (this.unboxObject) {
            return result.getValue(env);
        } else {
            return result.deref(env);
        }
    }
}
