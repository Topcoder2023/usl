package com.gitee.usl.kernel.engine;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.AttributeMeta;
import com.gitee.usl.infra.structure.Plugins;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
public class Function extends AnnotatedFunction {
    private static final long serialVersionUID = 1538520882629592567L;

    public Function(FunctionDefinition definition) {
        super(definition);
    }

    public static FunctionBuilder newBuilder() {
        return new FunctionBuilder();
    }

    public static final class FunctionBuilder {
        private Class<?> clz;
        private Method method;
        private Object instance;
        private Plugins plugins;
        private String methodName;
        private List<String> nameList;
        private AttributeMeta attributeMeta;
        private final List<Function> functionList = new ArrayList<>(NumberConstant.EIGHT);

        public FunctionBuilder clazz(Class<?> clz) {
            this.clz = clz;
            return this;
        }

        public FunctionBuilder method(String name) {
            this.methodName = name;
            return this;
        }

        public FunctionBuilder plugin(Plugin plugin) {
            if (this.plugins == null) {
                this.plugins = new Plugins();
            }
            this.plugins.install(plugin);
            return this;
        }

        public FunctionBuilder names(String... names) {
            this.nameList = Stream.of(names).collect(Collectors.toList());
            return this;
        }

        public FunctionBuilder instance(Object instance) {
            this.instance = instance;
            return this;
        }

        public FunctionBuilder attributeMeta(AttributeMeta attributeMeta) {
            this.attributeMeta = attributeMeta;
            return this;
        }

        public FunctionBuilder next() {
            return this.next(true);
        }

        public FunctionBuilder next(boolean clear) {
            Assert.notBlank(methodName, "Please specify a method name");

            method = ReflectUtil.getMethodByName(clz, methodName);

            Assert.notNull(method, "The specified method was not found - {}", methodName);

            if (CollUtil.isEmpty(nameList)) {
                nameList.add(CharSequenceUtil.toUnderlineCase(methodName));
            }

            FunctionDefinition definition = new FunctionDefinition(nameList.get(NumberConstant.ZERO));

            if (nameList.size() > NumberConstant.ONE) {
                definition.alias().addAll(nameList.subList(NumberConstant.ONE, nameList.size()));
            }

            if (instance == null) {
                instance = ReflectUtil.newInstanceIfPossible(clz);
            }

            MethodMeta<?> methodMeta = new MethodMeta<>(instance, clz, method);

            definition.setMethodMeta(methodMeta);

            if (this.attributeMeta != null) {
                definition.attribute().insertAll(attributeMeta.asMap());
            }

            Function function = new Function(definition);

            if (plugins != null) {
                Plugins toInstall = function.plugins();
                plugins.visit(toInstall::install);
            }

            functionList.add(function);

            if (clear) {
                this.clear();
            }

            return this;
        }

        public List<AviatorFunction> buildAll() {
            // 还有尚未构建的函数
            if (this.clz != null) {
                this.next();
            }

            return this.functionList.stream()
                    .map(AviatorFunction.class::cast)
                    .collect(Collectors.toList());
        }

        private void clear() {
            this.clz = null;
            this.method = null;
            this.plugins = null;
            this.instance = null;
            this.nameList = null;
            this.methodName = null;
            this.attributeMeta = null;
        }
    }
}
