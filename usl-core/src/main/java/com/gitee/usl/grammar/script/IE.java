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

    private final Map<VariableMeta, AviatorJavaType> variables = new IdentityHashMap<>();

    private final Map<Token<?>, AviatorObject> constantPool = new IdentityHashMap<>();

    public IE(final ScriptEngine instance,
              final List<VariableMeta> vars,
              final ScriptKeyword symbolTable,
              final List<IR> instruments,
              final boolean unboxObject) {
        super(instance, vars, symbolTable);
        this.instruments = instruments;
        this.unboxObject = unboxObject;
        loadVars(vars);
        loadConstants(Collections.emptySet(), instruments);
    }

    private void loadVars(final List<VariableMeta> vars) {
        for (VariableMeta v : vars) {
            this.variables.put(v, new AviatorJavaType(v.getName(), this.symbolTable));
        }
    }

    private void loadConstants(final Set<Token<?>> constants, final List<IR> instruments) {
        final Env env = new Env();
        env.setInstance(this.instance);
        InterpretContext ctx = new InterpretContext(this, instruments, env);
        for (Token<?> token : constants) {
            final LoadIR loadConstantIR = new LoadIR(null, token, null, false);
            loadConstantIR.evalWithoutDispatch(ctx);
            this.constantPool.put(token, ctx.pop());
        }
    }

    public AviatorJavaType loadVar(final VariableMeta v) {
        return this.variables.get(v);
    }

    public AviatorObject loadConstant(final Token<?> token) {
        return this.constantPool.get(token);
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
