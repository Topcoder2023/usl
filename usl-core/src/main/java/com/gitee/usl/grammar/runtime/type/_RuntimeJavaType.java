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
package com.gitee.usl.grammar.runtime.type;

import com.gitee.usl.grammar.utils.Reflector;
import com.gitee.usl.grammar.utils.VarNameGenerator;

import java.util.Map;
import java.util.concurrent.Callable;


/**
 * Aviator runtime java type,used by when generate runtime result.
 *
 * @author dennis
 *
 */
public class _RuntimeJavaType extends _JavaType {

    public static final ThreadLocal<VarNameGenerator> TEMP_VAR_GEN =
            new ThreadLocal<VarNameGenerator>() {

                @Override
                protected VarNameGenerator initialValue() {
                    return new VarNameGenerator();
                }

            };

    protected Object object;
    protected Callable<Object> callable;

    public static _Object valueOf(final Object object) {
        if (object == null) {
            return _Null.NIL;
        }
        if (object instanceof _Object) {
            return (_Object) object;
        }
        return new _RuntimeJavaType(object);
    }

    /**
     * Deprecated since 5.0.0, please use {@link _RuntimeJavaType#valueOf(Object)} instead.
     *
     * @deprecated
     * @param object
     */
    @Deprecated
    public _RuntimeJavaType(final Object object) {
        super(null);
        this.object = object;
    }

    public Callable<Object> getCallable() {
        return this.callable;
    }

    public void setCallable(final Callable<Object> callable) {
        this.callable = callable;
    }

    public static String genName() {
        return TEMP_VAR_GEN.get().gen();
    }

    @Override
    public String getName() {
        if (this.name == null) {
            this.name = genName();
        }
        return this.name;
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        if (this.callable != null) {
            try {
                return this.callable.call();
            } catch (Exception e) {
                throw Reflector.sneakyThrow(e);
            }
        }
        return this.object;
    }

}
