package com.googlecode.aviator.lexer;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.ScriptKeyword;
import com.googlecode.aviator.Feature;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.exception.CompileExpressionErrorException;
import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;
import com.googlecode.aviator.lexer.token.*;
import com.googlecode.aviator.utils.CommonUtils;
import com.googlecode.aviator.utils.Constants;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.LinkedList;

/**
 * @author hongda.li
 */
@Description("词法分析器")
public class ExpressionLexer {
    private static final long OVERFLOW_FLAG = Long.MAX_VALUE / 10;
    private static final long OVERFLOW_SINGLE = Long.MAX_VALUE % 10;
    // current char
    private char peek;
    // Char iterator for string
    private final CharacterIterator iterator;
    @Getter
    @Setter
    private int lineNo;

    @Getter
    private final ScriptKeyword symbolTable;
    // Tokens buffer
    private LinkedList<Token<?>> tokenBuffer;
    private final ScriptEngine instance;
    private final String expression;
    private final MathContext mathContext;
    private final boolean parseFloatIntoDecimal;
    private final boolean parseIntegralNumberIntoDecimal;

    public ExpressionLexer(final ScriptEngine instance, final String expression) {
        this.lineNo = 1;
        this.instance = instance;
        this.expression = expression;
        this.iterator = new StringCharacterIterator(expression);
        this.peek = this.iterator.current();
        this.symbolTable = new ScriptKeyword();
        this.mathContext = this.instance.getOptionValue(Options.MATH_CONTEXT).mathContext;
        this.parseFloatIntoDecimal = this.instance.getOptionValue(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL).bool;
        this.parseIntegralNumberIntoDecimal = this.instance.getOptionValue(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL).bool;
    }

    @Description("保存Token")
    public void pushback(final Token<?> token) {
        if (this.tokenBuffer == null) {
            this.tokenBuffer = new LinkedList<>();
        }
        this.tokenBuffer.push(token);
    }

    public void nextChar() {
        this.peek = this.iterator.next();
    }

    public void prevChar() {
        this.peek = this.iterator.previous();
    }

    public int getCurrentIndex() {
        return this.iterator.getIndex();
    }

    public Token<?> scan() {
        return this.scan(true);
    }

    public Token<?> scan(final boolean analyse) {
        if (this.tokenBuffer != null && !this.tokenBuffer.isEmpty()) {
            return this.tokenBuffer.pop();
        }

        for (; ; nextChar()) {
            if (this.peek == CharacterIterator.DONE) {
                return null;
            }

            if (analyse) {
                if (this.peek == ' ' || this.peek == '\t' || this.peek == '\r' || this.peek == '\n') {
                    if (this.peek == '\n') {
                        this.lineNo++;
                    }
                    continue;
                }
                break;
            } else {
                char ch = this.peek;
                int index = this.iterator.getIndex();
                nextChar();
                return new CharToken(ch, this.lineNo, index);
            }
        }

        @Description("十六进制标识")
        boolean isHex = Character.isDigit(this.peek) && this.peek == '0';
        if (isHex) {
            nextChar();
            if (this.peek == 'x' || this.peek == 'X') {
                nextChar();
                StringBuilder builder = new StringBuilder();
                int startIndex = this.iterator.getIndex() - 2;
                long value = 0L;
                do {
                    builder.append(this.peek);
                    value = 16 * value + Character.digit(this.peek, 16);
                    nextChar();
                } while (CommonUtils.isValidHexChar(this.peek));
                return new NumberToken(value, builder.toString(), this.lineNo, startIndex);
            } else {
                prevChar();
            }
        }

        // If it is a digit
        if (Character.isDigit(this.peek) || this.peek == '.') {
            StringBuilder sb = new StringBuilder();
            int startIndex = this.iterator.getIndex();
            long lval = 0L;

            double dval = 0d;
            boolean hasDot = false;
            double d = 10.0;
            boolean isBigInt = false;
            boolean isBigDecimal = false;
            boolean scientificNotation = false;
            boolean negExp = false;
            boolean isOverflow = false;
            do {
                sb.append(this.peek);
                if (this.peek == '.') {
                    if (scientificNotation) {
                        throw new CompileExpressionErrorException(
                                "Illegal number " + sb + " at " + this.iterator.getIndex());
                    }
                    if (hasDot) {
                        throw new CompileExpressionErrorException(
                                "Illegal Number " + sb + " at " + this.iterator.getIndex());
                    } else {
                        hasDot = true;
                        nextChar();
                    }
                } else if (this.peek == 'N') {
                    // big integer
                    if (hasDot) {
                        throw new CompileExpressionErrorException(
                                "Illegal number " + sb + " at " + this.iterator.getIndex());
                    }
                    isBigInt = true;
                    nextChar();
                    break;
                } else if (this.peek == 'M') {
                    isBigDecimal = true;
                    nextChar();
                    break;
                } else if (this.peek == 'e' || this.peek == 'E') {
                    if (scientificNotation) {
                        throw new CompileExpressionErrorException(
                                "Illegal number " + sb + " at " + this.iterator.getIndex());
                    }
                    scientificNotation = true;
                    nextChar();
                    if (this.peek == '-') {
                        negExp = true;
                        sb.append(this.peek);
                        nextChar();
                    }
                } else {
                    int digit = Character.digit(this.peek, 10);
                    if (scientificNotation) {
                        int n = digit;
                        nextChar();
                        while (Character.isDigit(this.peek)) {
                            sb.append(this.peek);
                            n = 10 * n + Character.digit(this.peek, 10);
                            nextChar();
                        }
                        while (n-- > 0) {
                            if (negExp) {
                                dval = dval / 10;
                            } else {
                                dval = 10 * dval;
                            }
                        }
                        hasDot = true;
                    } else if (hasDot) {
                        dval = dval + digit / d;
                        d = d * 10;
                        nextChar();
                    } else {
                        if (!isOverflow && (lval > OVERFLOW_FLAG || (lval == OVERFLOW_FLAG && digit > OVERFLOW_SINGLE))) {
                            isOverflow = true;
                        }
                        lval = 10 * lval + digit;
                        dval = 10 * dval + digit;
                        nextChar();
                    }
                }

            } while (Character.isDigit(this.peek)
                    || this.peek == '.'
                    || this.peek == 'E'
                    || this.peek == 'e'
                    || this.peek == 'M'
                    || this.peek == 'N');

            Number value;
            if (isBigDecimal) {
                value = new BigDecimal(getBigNumberLexeme(sb), this.mathContext);
            } else if (isBigInt) {
                value = new BigInteger(getBigNumberLexeme(sb));
            } else if (hasDot) {
                if (this.parseFloatIntoDecimal && sb.length() > 1) {
                    value = new BigDecimal(sb.toString(), this.mathContext);
                } else if (sb.length() == 1) {
                    // only have a dot character.
                    return new CharToken('.', this.lineNo, startIndex);
                } else {
                    value = dval;
                }
            } else {
                if (this.parseIntegralNumberIntoDecimal) {
                    // we make integral number as a BigDecimal.
                    value = new BigDecimal(sb.toString(), this.mathContext);
                } else {
                    // The long value is overflow, we should prompt it to be a BigInteger.
                    if (isOverflow) {
                        value = new BigInteger(sb.toString());
                    } else {
                        value = lval;
                    }
                }
            }
            String lexeme = sb.toString();
            if (isBigDecimal || isBigInt) {
                lexeme = lexeme.substring(0, lexeme.length() - 1);
            }
            return new NumberToken(value, lexeme, this.lineNo, startIndex);
        }

        // It is a variable
        if (this.peek == '#') {
            int startIndex = this.iterator.getIndex();
            nextChar(); // skip '#'
            boolean hasBackquote = false;

            if (this.peek == '#') {
                // ## comments
                while (this.peek != CharacterIterator.DONE && this.peek != '\n') {
                    nextChar();
                }
                return this.scan(analyse);
            } else if (this.peek == '`') {
                hasBackquote = true;
                nextChar();
            }

            StringBuilder sb = new StringBuilder();

            if (hasBackquote) {
                while (this.peek != '`') {
                    if (this.peek == CharacterIterator.DONE) {
                        throw new CompileExpressionErrorException(
                                "EOF while reading string at index: " + this.iterator.getIndex());
                    }
                    sb.append(this.peek);
                    nextChar();
                }
                // skip '`'
                nextChar();
            } else {
                while (Character.isJavaIdentifierPart(this.peek) || this.peek == '.' || this.peek == '['
                        || this.peek == ']') {
                    sb.append(this.peek);
                    nextChar();
                }
            }
            String lexeme = sb.toString();
            if (lexeme.isEmpty()) {
                throw new ExpressionSyntaxErrorException("Blank variable name after '#'");
            }
            Variable variable = new Variable(lexeme, this.lineNo, startIndex);
            variable.setQuote(true);
            return this.symbolTable.reserve(variable);
        }
        if (Character.isJavaIdentifierStart(this.peek)) {
            int startIndex = this.iterator.getIndex();
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(this.peek);
                nextChar();
            } while (Character.isJavaIdentifierPart(this.peek) || this.peek == '.');
            String lexeme = sb.toString();
            Variable variable = new Variable(lexeme, this.lineNo, startIndex);
            return this.symbolTable.reserve(variable);
        }

        if (CommonUtils.isBinaryOperator(this.peek)) {
            CharToken opToken = new CharToken(this.peek, this.lineNo, this.iterator.getIndex());
            nextChar();
            return opToken;
        }

        // String
        if (this.peek == '"' || this.peek == '\'') {
            char left = this.peek;
            int startIndex = this.iterator.getIndex();
            StringBuilder sb = new StringBuilder();
            boolean hasInterpolation = false;
            // char prev = this.peek;
            while ((this.peek = this.iterator.next()) != left) {
                // It's not accurate,but acceptable.
                if (this.peek == '#' && !hasInterpolation) {
                    hasInterpolation = true;
                }

                if (this.peek == '\\') { // escape
                    nextChar();
                    if (this.peek == CharacterIterator.DONE) {
                        throw new CompileExpressionErrorException(
                                "EOF while reading string at index: " + this.iterator.getIndex());
                    }
                    if (this.peek == left) {
                        sb.append(this.peek);
                        continue;
                    }
                    switch (this.peek) {
                        case 't':
                            this.peek = '\t';
                            break;
                        case 'r':
                            this.peek = '\r';
                            break;
                        case 'n':
                            this.peek = '\n';
                            break;
                        case '\\':
                            break;
                        case 'b':
                            this.peek = '\b';
                            break;
                        case 'f':
                            this.peek = '\f';
                            break;
                        case '#':
                            hasInterpolation = true;
                            if (this.instance.isFeatureEnabled(Feature.StringInterpolation)) {
                                sb.append('\\');
                                this.peek = '#';
                                break;
                            }
                        default: {
                            throw new CompileExpressionErrorException(
                                    "Unsupported escape character: \\" + this.peek);
                        }

                    }
                }

                if (this.peek == CharacterIterator.DONE) {
                    throw new CompileExpressionErrorException(
                            "EOF while reading string at index: " + this.iterator.getIndex());
                }

                sb.append(this.peek);
            }
            nextChar();
            return new StringToken(sb.toString(), this.lineNo, startIndex).withMeta(Constants.INTER_META,
                    hasInterpolation);
        }

        Token<Character> token = new CharToken(this.peek, this.lineNo, this.iterator.getIndex());
        nextChar();
        return token;
    }

    @Description("获取Token扫描值")
    public String getScanString() {
        Token<?> token = this.tokenBuffer != null ? this.tokenBuffer.peekFirst() : null;
        return this.expression.substring(0, token != null && token.getStartIndex() > 0
                ? token.getEndIndex()
                : this.iterator.getIndex());
    }

    @Description("获取数字的字面值")
    private String getBigNumberLexeme(final StringBuilder builder) {
        String lexeme = builder.toString();
        return lexeme.substring(0, lexeme.length() - 1);
    }

}
