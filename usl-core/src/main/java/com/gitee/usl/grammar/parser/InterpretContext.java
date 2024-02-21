package com.gitee.usl.grammar.parser;

import com.gitee.usl.grammar.script.IE;
import com.gitee.usl.grammar.lexer.token.Token;
import com.gitee.usl.grammar.runtime.type._Null;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Env;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;

/**
 * @author hongda.li
 */
@Getter
public class InterpretContext {

    private final Env env;
    private final IE expression;
    private final IR[] instruments;
    private final ArrayDeque<_Object> operands = new ArrayDeque<>();

    private IR pc;
    private int pcIndex = -1;
    private boolean reachEnd;

    public InterpretContext(final IE exp,
                            final List<IR> instruments,
                            final Env env) {
        this.expression = exp;
        this.instruments = instruments.toArray(new IR[]{});
        this.env = env;
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

    public void push(_Object arg) {
        this.push(arg, null);
    }

    public void push(_Object arg, Token<?> token) {
        this.operands.push(Optional.ofNullable(arg).orElse(new _Null()).setFrom(token));
    }

    public _Object peek() {
        return this.operands.peek();
    }

    public _Object pop() {
        return this.operands.pop();
    }

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
