package com.gitee.usl.grammar;

import java.util.HashMap;
import java.util.Map;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.structure.AwaitVariable;
import com.gitee.usl.infra.structure.FunctionVariable;
import com.gitee.usl.infra.structure.VarVariable;
import com.googlecode.aviator.lexer.token.Token;
import com.googlecode.aviator.lexer.token.Variable;
import lombok.Getter;

/**
 * @author hongda.li
 */
@Getter
@Description("关键字变量表")
public class ScriptKeyword {

    @Description("变量表")
    private final Map<String, Variable> table = new HashMap<>();

    @Description("关键字")
    private static final Map<String, Variable> RESERVED = new HashMap<>();

    @Description("定义关键字")
    private static void reserveKeyword(final Variable v) {
        RESERVED.put(v.getLexeme(), v);
    }

    static {
        reserveKeyword(Variable.TRUE);
        reserveKeyword(Variable.FALSE);
        reserveKeyword(Variable.NIL);

        reserveKeyword(Variable.LAMBDA);
        reserveKeyword(Variable.FN);

        // 兼容 function 关键字 <==> fn
        reserveKeyword(FunctionVariable.getInstance());

        reserveKeyword(Variable.END);
        reserveKeyword(Variable.IF);
        reserveKeyword(Variable.ELSE);
        reserveKeyword(Variable.FOR);
        reserveKeyword(Variable.IN);
        reserveKeyword(Variable.RETURN);

        // 兼容 await 关键字 <==> return
        reserveKeyword(AwaitVariable.getInstance());

        reserveKeyword(Variable.BREAK);
        reserveKeyword(Variable.CONTINUE);
        reserveKeyword(Variable.LET);

        // 兼容 var 关键字 <==> let
        reserveKeyword(VarVariable.getInstance());

        reserveKeyword(Variable.WHILE);
        reserveKeyword(Variable.ELSIF);
        reserveKeyword(Variable.TRY);
        reserveKeyword(Variable.CATCH);
        reserveKeyword(Variable.FINALLY);
        reserveKeyword(Variable.THROW);
        reserveKeyword(Variable.NEW);
        reserveKeyword(Variable.USE);
    }

    public static boolean isReservedKeyword(final String name) {
        return RESERVED.containsKey(name);
    }

    public static boolean isReservedKeyword(final Variable v) {
        return isReservedKeyword(v.getLexeme());
    }

    public boolean isReserved(final String name) {
        return isReservedKeyword(name) || this.table.containsKey(name);
    }

    public static Variable tryReserveKeyword(final Variable var) {
        Variable reserve = RESERVED.get(var.getLexeme());
        return reserve != null ? reserve : var;
    }

    public Variable getVariable(final String name) {
        Variable var = RESERVED.get(name);
        return var != null ? var : this.table.get(name);
    }

    public Variable reserve(final String lexeme) {
        if (isReserved(lexeme)) {
            return getVariable(lexeme);
        } else {
            final Variable var = new Variable(lexeme, 0, -1);
            this.table.put(lexeme, var);
            return var;
        }
    }

    public Token<?> reserve(final Variable variable) {
        String lexeme = variable.getLexeme();
        if (isReserved(lexeme)) {
            Variable v = getVariable(lexeme);
            if (v.getStartIndex() < 0) {
                return v;
            }
            variable.setLexeme(v.getLexeme());
        } else {
            this.table.put(lexeme, variable);
        }
        return variable;
    }
}
