package com.gitee.usl.grammar.lexer.token;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author hongda.li
 */
@SuppressWarnings("unchecked")
@ToString
@EqualsAndHashCode
public abstract class AbstractToken<T> implements Token<T> {

    private final int lineIndex;

    private final int lineNo;

    @Override
    public int getLineNo() {
        return this.lineNo;
    }

    protected String lexeme;

    @Setter
    private Map<String, Object> metaMap;

    public AbstractToken(final String lexeme, final int lineNo, final int lineIndex) {
        this.lineNo = lineNo;
        this.lexeme = lexeme;
        this.lineIndex = lineIndex;
    }

    @Override
    public Map<String, Object> getMetaMap() {
        return this.metaMap;
    }

    @Override
    public Token<T> withMeta(final String name, final Object v) {
        if (this.metaMap == null) {
            this.metaMap = new IdentityHashMap<>();
        }
        this.metaMap.put(name, v);
        return this;
    }

    @Override
    public <V> V getMeta(final String name, final V defaultVal) {
        if (this.metaMap == null) {
            return defaultVal;
        }
        V val = (V) this.metaMap.get(name);
        if (val == null) {
            return defaultVal;
        }
        return val;
    }

    @Override
    public <V> V getMeta(final String name) {
        if (this.metaMap == null) {
            return null;
        }
        return (V) this.metaMap.get(name);
    }

    @Override
    public String getLexeme() {
        return this.lexeme;
    }

    @Override
    public int getStartIndex() {
        return this.lineIndex;
    }

    @Override
    public int getEndIndex() {
        return this.lineIndex + (this.lexeme != null ? this.lexeme.length() : 0);
    }

}
