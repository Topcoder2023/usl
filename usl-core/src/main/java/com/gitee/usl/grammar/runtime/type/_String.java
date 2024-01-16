package com.gitee.usl.grammar.runtime.type;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.gitee.usl.grammar.utils.TypeUtils;

/**
 * A aviator string
 *
 * @author dennis
 */
public class _String extends _Object {

    private static final long serialVersionUID = -7430694306919959899L;
    private final String lexeme;
    private final boolean isLiteral;
    private boolean hasInterpolation = true; // default must be true to avoid corner cases;
    private int lineNo;

    @Override
    public String desc(final Map<String, Object> env) {
        Object val = this.getLexeme(env, false);
        if (val != this) {
            return "<" + getAviatorType() + ", " + val + ">";
        } else {
            return "<" + getAviatorType() + ", this>";
        }
    }

    @Override
    public _Type getAviatorType() {
        return _Type.String;
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        return getLexeme(env);
    }

    public _String(final String lexeme) {
        this(lexeme, false);
    }

    public _String(final String lexeme, final boolean isLiteral) {
        super();
        this.lexeme = lexeme;
        this.isLiteral = isLiteral;
    }

    public _String(final String lexeme, final boolean isLiteral, final boolean hasInterpolation,
                   final int lineNo) {
        super();
        this.lexeme = lexeme;
        this.isLiteral = isLiteral;
        this.hasInterpolation = hasInterpolation;
        this.lineNo = lineNo;
    }

    @Override
    public _Object add(final _Object other, final Map<String, Object> env) {
        final StringBuilder sb = new StringBuilder(getLexeme(env));

        if (other.getAviatorType() != _Type.Pattern) {
            sb.append(other.getValue(env));
        } else {
            final _Pattern otherPatterh = (_Pattern) other;
            sb.append(otherPatterh.pattern.pattern());
        }
        return new _StringBuilder(sb);
    }

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER =
            new ThreadLocal<SimpleDateFormat>() {

                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
                }

            };

    private int tryCompareDate(final Map<String, Object> env, final Date otherDate) {
        try {
            final SimpleDateFormat simpleDateFormat = DATE_FORMATTER.get();
            final Date thisDate = simpleDateFormat.parse(getLexeme(env));
            return thisDate.compareTo(otherDate);
        } catch (final Throwable t) {
            throw new ExpressionRuntimeException("Compare date error", t);
        }
    }

    @Override
    public int innerCompare(final _Object other, final Map<String, Object> env) {
        final String left = getLexeme(env);

        if (other.getAviatorType() == _Type.String) {
            final _String otherString = (_String) other;
            final String right = otherString.getLexeme(env);
            if (left != null && right != null) {
                return left.compareTo(right);
            } else if (left == null && right != null) {
                return -1;
            } else if (left != null && right == null) {
                return 1;
            } else {
                return 0;
            }
        }

        switch (other.getAviatorType()) {
            case JavaType:
                final _JavaType javaType = (_JavaType) other;
                final Object otherJavaValue = javaType.getValue(env);
                if (left == null && otherJavaValue == null) {
                    return 0;
                } else if (left != null && otherJavaValue == null) {
                    return 1;
                }
                if (TypeUtils.isString(otherJavaValue)) {
                    if (left == null) {
                        return -1;
                    } else {
                        return left.compareTo(String.valueOf(otherJavaValue));
                    }
                } else if (otherJavaValue instanceof Date) {
                    return tryCompareDate(env, (Date) otherJavaValue);
                } else {
                    throw new CompareNotSupportedException(
                            "Could not compare " + desc(env) + " with " + other.desc(env));
                }
            case Nil:
                if (left == null) {
                    return 0;
                } else {
                    return 1;
                }
            default:
                throw new CompareNotSupportedException(
                        "Could not compare " + desc(env) + " with " + other.desc(env));
        }
    }

    public String getLexeme(final Map<String, Object> env) {
        return this.getLexeme(env, true);
    }

    public String getLexeme(final Map<String, Object> env, final boolean warnOnCompile) {
        return this.lexeme;
    }

}
