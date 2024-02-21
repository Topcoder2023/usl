/**
 * Copyright (C) 2010 dennis zhuang (killme2008@gmail.com)
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 **/
package com.gitee.usl.grammar.runtime.function;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.infra.constant.AsmConstants;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.runtime.FunctionArgument;
import com.gitee.usl.grammar.runtime.type._Bool;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.runtime.type._JavaType;
import com.gitee.usl.grammar.runtime.type._Null;
import com.gitee.usl.grammar.runtime.type._Number;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.runtime.type._Pattern;
import com.gitee.usl.grammar.runtime.type._RuntimeJavaType;
import com.gitee.usl.grammar.runtime.type._String;
import com.gitee.usl.grammar.runtime.type._Type;


/**
 * Function helper
 *
 * @author dennis
 */
public class FunctionUtils {

    /**
     * Retrieve the invocation arguments info from env, returns null when absent.
     *
     * @param env
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<FunctionArgument> getFunctionArguments(final Map<String, Object> env) {
        return (List<FunctionArgument>) env.get(AsmConstants.FUNC_PARAMS_VAR);
    }

    /**
     * Get boolean value from env.
     *
     * @param arg the var name
     * @param env
     * @return
     */
    public static boolean getBooleanValue(final _Object arg,
                                          final Map<String, Object> env) {
        return (boolean) arg.getValue(env);
    }

    /**
     * Get string value from env.
     *
     * @param arg the var name
     * @param env
     * @return
     */
    public static String getStringValue(final _Object arg,
                                        final Map<String, Object> env) {
        String result = null;

        final Object value = arg.getValue(env);
        if (value instanceof Character) {
            result = value.toString();
        } else {
            result = (String) value;
        }
        return result;
    }

    /**
     * get a object from env
     *
     * @param arg the var name
     * @param env
     * @return
     */
    public static Object getJavaObject(final _Object arg, final Map<String, Object> env) {
        if (arg.getAviatorType() != _Type.JavaType) {
            throw new ClassCastException(arg.desc(env) + " is not a javaType");
        }
        return ((_JavaType) arg).getValue(env);
    }


    /**
     * Get a function from env in follow orders:
     * <ul>
     * <li>arg value</li>
     * <li>env</li>
     * <li>current evaluator instance.</li>
     * </ul>
     *
     * @param arg
     * @param env
     * @param arity
     * @return
     */
    public static _Function getFunction(final _Object arg, final Map<String, Object> env,
                                        final int arity) {
        if (arg.getAviatorType() != _Type.JavaType
                && arg.getAviatorType() != _Type.Lambda) {
            throw new ClassCastException(arg.desc(env) + " is not a function");
        }
        // Runtime type.
        Object val = null;
        if (arg instanceof _Function) {
            return (_Function) arg;
        }

        if (arg instanceof _RuntimeJavaType
                && (val = arg.getValue(env)) instanceof _Function) {
            return (_Function) val;
        }

        // resolve by name.
        // special processing for "-" operator
        String name = ((_JavaType) arg).getName();
        if (name.equals("-")) {
            if (arity == 2) {
                name = "-sub";
            } else {
                name = "-neg";
            }
        }
        _Function rt = null;
        if (env != null) {
            rt = (_Function) env.get(name);
        }
        if (rt == null) {
            ScriptEngine instance = RuntimeUtils.getInstance(env);
            rt = instance.getFunction(name);
        }
        return rt;
    }


    /**
     * Get a number from env.
     *
     * @param arg1 the var
     * @param env
     * @return
     */
    public static Number getNumberValue(final _Object arg1,
                                        final Map<String, Object> env) {
        return (Number) arg1.getValue(env);
    }


    /**
     * Wraps the object as aviator object.
     *
     * @param ret the java object
     * @return wrapped aviator object
     * @since 4.2.5
     */
    public static _Object wrapReturn(final Object ret) {
        if (ret == null) {
            return _Null.NIL;
        } else if (ret instanceof Pattern) {
            return new _Pattern((Pattern) ret);
        } else if (ret instanceof Number) {
            return _Number.valueOf(ret);
        } else if (ret instanceof CharSequence || ret instanceof Character) {
            return new _String(ret.toString());
        } else if (ret instanceof Boolean) {
            return _Bool.valueOf((boolean) ret);
        } else if (ret instanceof _Object) {
            return (_Object) ret;
        } else {
            return _RuntimeJavaType.valueOf(ret);
        }
    }

}
