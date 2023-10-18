package com.gitee.usl.kernel.engine;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * 手动实现注解的注解函数
 *
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
        private boolean finished;
        private String methodName;
        private List<Plugin> plugins;
        private List<String> nameList;
        private AttributeMeta attributeMeta;
        private UnaryOperator<String> mapping;
        private final List<Function> functionList = new ArrayList<>(NumberConstant.EIGHT);

        public FunctionBuilder clazz(Class<?> clz) {
            this.clz = clz;
            finished = false;
            return this;
        }

        public FunctionBuilder method(String name) {
            this.methodName = name;
            finished = false;
            return this;
        }

        public FunctionBuilder plugin(Plugin plugin) {
            if (this.plugins == null) {
                this.plugins = new ArrayList<>(NumberConstant.FIVE);
            }
            this.plugins.add(plugin);
            finished = false;
            return this;
        }

        public FunctionBuilder names(String... names) {
            this.nameList = Arrays.asList(names);
            finished = false;
            return this;
        }

        public FunctionBuilder mapping(UnaryOperator<String> mapping) {
            this.mapping = mapping;
            finished = false;
            return this;
        }

        public FunctionBuilder instance(Object instance) {
            this.instance = instance;
            finished = false;
            return this;
        }

        public FunctionBuilder attributeMeta(AttributeMeta attributeMeta) {
            this.attributeMeta = attributeMeta;
            finished = false;
            return this;
        }

        public FunctionBuilder next() {
            return this.next(true);
        }

        public FunctionBuilder next(boolean reuse) {
            // 方法名称不能为空
            // 当存在多个同名方法时，默认取第一个
            // 因此存在多个同名方法不建议使用此模式构造函数
            Assert.notBlank(methodName, "Please specify a method name");

            // 根据方法名称获取指定方法
            method = ReflectUtil.getMethodByName(clz, methodName);

            // 方法不能为空，防止无效的方法名称
            Assert.notNull(method, "The specified method was not found - {}", methodName);

            // 如果没有指定了任何函数名
            if (nameList == null) {
                // 默认使用方法名转下划线的策略
                if (mapping == null) {
                    mapping = CharSequenceUtil::toUnderlineCase;
                }
                // 根据映射策略默认生成一个可用的函数名称
                nameList = Collections.singletonList(mapping.apply(methodName));
            }

            // 将首个可用的函数名称转为函数定义信息
            FunctionDefinition definition = new FunctionDefinition(nameList.get(NumberConstant.ZERO));

            // 如果存在函数别名，将函数别名添加到函数定义信息中
            if (nameList.size() > NumberConstant.ONE) {
                definition.alias().addAll(nameList.subList(NumberConstant.ONE, nameList.size()));
            }

            // 如果实例可以被构造，则创建默认实例
            if (instance == null) {
                instance = ReflectUtil.newInstanceIfPossible(clz);
            }

            // 将实例、类、方法封装成方法元数据
            MethodMeta<?> methodMeta = new MethodMeta<>(instance, clz, method);

            // 将方法元数据保存到函数定义信息中
            definition.setMethodMeta(methodMeta);

            // 如果额外属性不为空，也保存到函数定义信息中
            if (attributeMeta != null) {
                definition.attribute().insertAll(attributeMeta.asMap());
            }

            // 根据函数定义信息构造函数
            Function function = new Function(definition);

            // 如果插件不为空，则为函数添加插件
            if (plugins != null) {
                Plugins toInstall = function.plugins();
                plugins.forEach(toInstall::install);
            }

            // 将构造好的函数保存到函数集合中
            functionList.add(function);
            finished = true;

            // 如果设置不复用构造信息，则将所有变量置空
            if (!reuse) {
                this.clear();
            }

            // 函数名称和方法名称不可能复用，因此强制清空
            this.nameList = null;
            this.methodName = null;

            return this;
        }

        public List<AviatorFunction> buildAll() {
            // 还有尚未构建的函数
            if (!finished) {
                this.next();
            }

            // 将函数转为标准的 AviatorFunction，便于后续注册
            return this.functionList.stream()
                    .map(AviatorFunction.class::cast)
                    .collect(Collectors.toList());
        }

        private void clear() {
            this.clz = null;
            this.method = null;
            this.plugins = null;
            this.instance = null;
            this.attributeMeta = null;
        }
    }
}
