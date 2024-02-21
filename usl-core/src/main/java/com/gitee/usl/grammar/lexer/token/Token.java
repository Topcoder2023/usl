package com.gitee.usl.grammar.lexer.token;

import java.util.Map;

/**
 * @author hongda.li
 */
public interface Token<T> {
    enum TokenType {
        String, Variable, Number, Char, Operator, Pattern, Delegate
    }

    Token<T> withMeta(String name, Object v);

    Map<String, Object> getMetaMap();

    <V> V getMeta(final String name, final V defaultVal);

    <V> V getMeta(final String name);

    T getValue(Map<String, Object> env);

    TokenType getType();

    String getLexeme();

    int getStartIndex();

    int getEndIndex();

    int getLineNo();
}
