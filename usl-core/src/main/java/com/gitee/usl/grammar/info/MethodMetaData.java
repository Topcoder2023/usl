package com.gitee.usl.grammar.info;

import com.gitee.usl.grammar.lexer.token.Token;

/**
 * @author hongda.li
 */
public class MethodMetaData {
    public int parameterCount = 0;

    public final Token<?> token;
    public final String methodName;
    public int funcId = -1;
    public MethodMetaData(final Token<?> token, final String methodName) {
        this.token = token;
        this.methodName = methodName;
    }

}
