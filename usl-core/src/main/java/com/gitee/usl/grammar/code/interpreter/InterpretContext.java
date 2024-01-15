package com.gitee.usl.grammar.code.interpreter;

import com.gitee.usl.grammar.script.IE;
import com.gitee.usl.grammar.lexer.token.Token;
import com.gitee.usl.grammar.parser.VariableMeta;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.runtime.type.AviatorNil;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Eval IR context.
 *
 * @author dennis(killme2008 @ gmail.com)
 */
public class InterpretContext {
    private final ArrayDeque<AviatorObject> operands = new ArrayDeque<>();
    @Getter
    private IR pc;
    private int pcIndex = -1;
    private IR[] instruments = new IR[0];
    @Getter
    private final Env env;
    @Getter
    private final IE expression;
    private boolean reachEnd;
    private final boolean trace;


    public InterpretContext(final IE exp, final List<IR> instruments,
                            final Env env) {
        this.expression = exp;
        this.instruments = instruments.toArray(this.instruments);
        this.env = env;
        this.trace = RuntimeUtils.isTracedEval(env);
        next();
    }


    public void clearStack() {
        this.operands.clear();
    }

    public void jumpTo(final int tpc) {
        if (tpc == this.instruments.length) {
            this.pc = null;
            this.pcIndex = -1;
            this.reachEnd = true;
            return;
        }
        this.pcIndex = tpc;
        this.pc = this.instruments[this.pcIndex];
    }

    public boolean next() {
        if (this.reachEnd) {
            return false;
        }
        this.pcIndex++;
        if (this.pcIndex < this.instruments.length) {
            this.pc = this.instruments[this.pcIndex];
            return true;
        }
        return false;
    }

    public void push(AviatorObject arg) {
        this.push(arg, null);
    }

    public void push(AviatorObject arg, Token<?> token) {
        if (arg == null) {
            arg = AviatorNil.NIL;
        }
        arg.setFrom(token);
        this.operands.push(arg);
    }

    public AviatorObject peek() {
        return this.operands.peek();
    }

    public AviatorObject pop() {
        return this.operands.pop();
    }

    public String descOperandsStack() {
        StringBuilder sb = new StringBuilder("<Stack, [");
        int i = this.operands.size();
        for (AviatorObject obj : this.operands) {
            sb.append(obj.desc(this.env));
            if (--i > 0) {
                sb.append(", ");
            }
        }
        sb.append("]>");
        return sb.toString();
    }

    /**
     * Move pc to next and execute it.
     */
    public void dispatch() {
        this.dispatch(true);
    }

    public void dispatch(final boolean next) {
        if (next && !next()) {
            return;
        }

        if (this.pc != null) {
            this.pc.eval(this);
        }
    }
}
