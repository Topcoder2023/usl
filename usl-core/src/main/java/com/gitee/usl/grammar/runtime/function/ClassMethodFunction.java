package com.gitee.usl.grammar.runtime.function;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;
import com.gitee.usl.grammar.utils.Reflector;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hongda.li
 */
@SystemFunction
public class ClassMethodFunction extends BasicFunction {

    private MethodHandle handle; // Only for one-arity function.
    private Class<?>[] pTypes;
    private final String name;
    private final String methodName;
    private List<Method> methods; // For reflection.
    private final Class<?> clazz;
    private final boolean isStatic;

    public ClassMethodFunction(final Class<?> clazz, final boolean isStatic, final String name,
                               final String methodName, final List<Method> methods)
            throws IllegalAccessException, NoSuchMethodException {
        this.name = name;
        this.clazz = clazz;
        this.isStatic = isStatic;
        this.methodName = methodName;

        init(isStatic, methodName, methods);
    }

    private void init(final boolean isStatic, final String methodName, final List<Method> methods)
            throws IllegalAccessException, NoSuchMethodException {
        if (methods.size() == 1) {
            // fast path by method handle.
            this.handle = MethodHandles.lookup().unreflect(methods.get(0)).asFixedArity();
            this.pTypes = methods.get(0).getParameterTypes();
            if (!isStatic) {
                Class<?>[] newTypes = new Class<?>[this.pTypes.length + 1];
                newTypes[0] = this.clazz;
                System.arraycopy(this.pTypes, 0, newTypes, 1, this.pTypes.length);
                this.pTypes = newTypes;
            }
            if (this.handle == null) {
                throw new NoSuchMethodException("Method handle for " + methodName + " not found");
            }
        } else {
            // Slow path by reflection
            this.methods = methods;
        }
    }

    private void writeObject(ObjectOutputStream output) throws IOException {
        output.writeObject(this.name);
        output.writeObject(this.clazz);
        output.writeBoolean(this.isStatic);
        output.writeObject(this.methodName);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public AviatorObject execute(Env env, AviatorObject... arguments) {
        Object[] jArgs = null;

        Object target = null;

        if (this.isStatic || this.handle != null) {
            jArgs = new Object[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                jArgs[i] = arguments[i].getValue(env);
            }
        } else {
            if (arguments.length < 1) {
                throw new IllegalArgumentException("Class<" + this.clazz + "> instance method "
                        + this.methodName + " needs at least one argument as instance.");
            }
            jArgs = new Object[arguments.length - 1];
            target = arguments[0].getValue(env);
            for (int i = 1; i < arguments.length; i++) {
                jArgs[i - 1] = arguments[i].getValue(env);
            }
        }

        if (this.handle != null) {
            try {
                return FunctionUtils
                        .wrapReturn(this.handle.invokeWithArguments(Reflector.boxArgs(this.pTypes, jArgs)));
            } catch (Throwable t) {
                throw Reflector.sneakyThrow(t);
            }
        } else {
            return FunctionUtils.wrapReturn(this.isStatic
                    ? Reflector.invokeStaticMethod(this.clazz, this.methodName, this.methods, jArgs)
                    : Reflector.invokeInstanceMethod(this.clazz, this.methodName, target, this.methods,
                    jArgs));
        }
    }

}
