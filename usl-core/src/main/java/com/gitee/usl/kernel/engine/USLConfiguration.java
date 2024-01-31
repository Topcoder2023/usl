package com.gitee.usl.kernel.engine;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.*;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.lexer.token.Variable;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.infra.structure.StringMap;
import com.gitee.usl.infra.structure.StringSet;
import com.gitee.usl.infra.utils.MethodInvokerOnMissing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * USL 配置类
 *
 * @author hongda.li
 */
@Data
@Slf4j
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class USLConfiguration extends StringMap<Object> {

    /**
     * 当前配置对应的实例
     */
    private USLRunner runner;

    /**
     * 脚本最大缓存容量
     * 默认采用 LRU 缓存算法
     */
    private Integer size;

    /**
     * 是否开启调试
     */
    private Boolean debug;

    /**
     * 是否开启对象方法调用
     * 启用后可通过 <对象名>.<方法名> 快速调用
     */
    private Boolean methodInvoke;

    /**
     * 缓存的失效时间
     * 默认为一小时后失效
     */
    private Duration expired;

    /**
     * 脚本引擎实例
     */
    private ScriptEngine engine;

    /**
     * 脚本编译器
     */
    private ScriptCompiler compiler;

    /**
     * 变量初始化器
     */
    private VariableDefinable definable;

    /**
     * 函数兜底机制
     */
    private FunctionMissing functionMissing;

    /**
     * 异常处理器
     */
    private ExceptionHandler exceptionHandler;

    /**
     * 函数包扫描路径
     */
    private final StringSet packageNameList = new StringSet();

    /**
     * 函数加载器
     */
    private final List<FunctionLoader> loaderList = new ArrayList<>();

    /**
     * 函数增强器
     */
    private final List<FunctionEnhancer> enhancers = new ArrayList<>();

    /**
     * 函数容器
     */
    private final FunctionHolder functionHolder = new FunctionHolder();

    /**
     * 添加指定类所在包类路径
     *
     * @param root 指定类
     * @return 链式调用
     */
    public USLConfiguration scan(Class<?> root) {
        Objects.requireNonNull(root);
        this.packageNameList.add(ClassUtil.getPackage(root));
        return this;
    }

    /**
     * 添加函数加载器
     *
     * @param loader 函数加载器实例
     * @return 函数加载器
     */
    public USLConfiguration loader(FunctionLoader loader) {
        Objects.requireNonNull(loader);
        this.loaderList.add(loader);
        return this;
    }

    /**
     * 添加函数增强器
     *
     * @param enhancer 函数增强器实例
     * @return 函数增强器
     */
    public USLConfiguration enhancer(FunctionEnhancer enhancer) {
        Objects.requireNonNull(enhancer);
        this.enhancers.add(enhancer);
        return this;
    }

    /**
     * 刷新配置
     */
    public void refresh() {
        log.debug("=========================" + this.runner.getName() + "=========================");
        log.debug("|| 执行器配置初始化");

        this.size = ObjectUtil.defaultIfNull(this.size, 2 << 10);
        log.debug("|| 脚本最大缓存容量 - {}", this.size);

        this.expired = ObjectUtil.defaultIfNull(this.expired, Duration.ofHours(1L));
        log.debug("|| 脚本缓存失效时间 - {}", this.expired);

        this.debug = ObjectUtil.defaultIfNull(this.debug, Boolean.FALSE);
        log.debug("|| 是否开启调试模式 - {}", this.debug);

        this.methodInvoke = ObjectUtil.defaultIfNull(this.methodInvoke, Boolean.TRUE);
        log.debug("|| 是否开启方法访问 - {}", this.methodInvoke);

        this.functionMissing = ObjectUtil.defaultIfNull(this.functionMissing, new MethodInvokerOnMissing(this.methodInvoke));
        log.debug("|| 函数兜底器实现类 - {}", this.functionMissing.getClass().getName());

        this.definable = ObjectUtil.defaultIfNull(this.definable, new VariableDefinable() {
            @Override
            public Object define(Variable variable) {
                return null;
            }
        });
        log.debug("|| 变量定义器实现类 - {}", this.definable.getClass().getName());

        this.exceptionHandler = ObjectUtil.defaultIfNull(this.exceptionHandler, ExceptionHandler.DEFAULT);
        log.debug("|| 异常处理器实现类 - {}", this.exceptionHandler.getClass().getName());

        this.packageNameList.forEach(name -> log.debug("|| 函数库扫描包路径 - {}", name));

        this.loaderList.forEach(loader -> log.debug("|| 函数加载器实现类 - {}", loader.getClass().getName()));

        this.enhancers.forEach(enhancer -> log.debug("|| 函数增强器实现类 - {}", enhancer.getClass().getName()));

        this.engine = new ScriptEngine(this);

        this.compiler = ObjectUtil.defaultIfNull(this.compiler, new BasicScriptCompiler(this));
        log.debug("|| 脚本编译器实现类 - {}", this.compiler.getClass().getName());

        log.debug("|| 配置初始化已完成");
        log.debug("==============================================================");

        // 根据函数加载器依次加载函数库
        loaderList.forEach(provider -> provider.load(this).forEach(function -> {
            if (function instanceof Definable) {
                Set<String> alias = ((Definable) function).definition().getAlias();
                functionHolder.register(function, alias);
            } else {
                functionHolder.register(function);
            }
        }));

        // 函数库全部加载完成后，由函数增强器列表依次增强函数
        functionHolder.onVisit(function -> enhancers.forEach(enhancer -> enhancer.enhance(function)));
    }

}
