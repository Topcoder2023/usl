package com.gitee.usl.kernel.engine;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.*;
import com.gitee.usl.api.impl.*;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.infra.structure.StringMap;
import com.gitee.usl.infra.structure.StringSet;
import com.gitee.usl.infra.structure.UniqueList;
import com.gitee.usl.infra.structure.wrapper.BoolWrapper;
import com.gitee.usl.infra.utils.LoggerHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
     * 是否已经刷新过配置
     */
    private Boolean refreshed;

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
     * 是否开启 If
     */
    private Boolean enableIf;

    /**
     * 是否开启循环
     */
    private Boolean enableLoop;

    /**
     * 是否开启调试
     */
    private Boolean enableDebug;

    /**
     * 是否开启 Return
     */
    private Boolean enableReturn;

    /**
     * 是否开启赋值
     */
    private Boolean enableAssign;

    /**
     * 是否开启匿名函数
     */
    private Boolean enableLambda;

    /**
     * 是否开启对象方法调用
     * 启用后可通过 <对象名>.<方法名> 快速调用
     */
    private Boolean enableMethodInvoke;

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
     * 服务发现
     */
    private ServiceFinder serviceFinder;

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
     * CLI交互
     */
    private CliInteractive cliInteractive;

    /**
     * WEB交互
     */
    private WebInteractive webInteractive;

    /**
     * 函数包扫描路径
     */
    private final StringSet packageNameList = new StringSet();

    /**
     * 日志级别
     */
    private final StringMap<Level> loggerLevel = new StringMap<>();

    /**
     * 函数加载器
     */
    private final List<FunctionLoader> loaders = new UniqueList<>();

    /**
     * 函数过滤器
     */
    private final List<FunctionFilter> filters = new UniqueList<>();

    /**
     * 函数增强器
     */
    private final List<FunctionEnhancer> enhancers = new UniqueList<>();

    /**
     * 函数容器
     */
    private final FunctionHolder functionHolder = new FunctionHolder();

    /**
     * 添加指定类所在包路径
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
     * 排除指定类所在包路径
     *
     * @param root 指定类
     * @return 链式调用
     */
    public USLConfiguration exclude(Class<?> root) {
        Objects.requireNonNull(root);
        String packageName = ClassUtil.getPackage(root);
        this.packageNameList.removeIf(name -> CharSequenceUtil.startWith(name, packageName));
        return this;
    }

    /**
     * 添加函数加载器
     *
     * @param loader 函数加载器实例
     * @return 链式调用
     */
    public USLConfiguration loader(FunctionLoader loader) {
        this.loaders.add(loader);
        return this;
    }

    /**
     * 添加函数增强器
     *
     * @param enhancer 函数增强器实例
     * @return 链式调用
     */
    public USLConfiguration enhancer(FunctionEnhancer enhancer) {
        this.enhancers.add(enhancer);
        return this;
    }

    /**
     * 添加函数过滤器
     *
     * @param filter 函数过滤器
     * @return 链式调用
     */
    public USLConfiguration filter(FunctionFilter filter) {
        this.filters.add(filter);
        return this;
    }

    /**
     * 设置日志级别
     *
     * @param name  日志名称
     * @param level 日志级别
     * @return 链式调用
     */
    public USLConfiguration loggerLevel(String name, Level level) {
        this.loggerLevel.put(name, level);
        return this;
    }

    /**
     * 刷新配置
     */
    public synchronized void refresh() {
        // 若已经刷新过配置类，则跳过刷新
        if (Boolean.TRUE.equals(this.refreshed)) {
            return;
        }

        // 重置日志级别
        LoggerHelper.resetLevel(this.loggerLevel);

        log.debug("USL Runner Version - {}", USLRunner.VERSION);

        this.size = ObjectUtil.defaultIfNull(this.size, 2 << 10);
        log.debug("脚本最大缓存容量 - {}", this.size);

        this.expired = ObjectUtil.defaultIfNull(this.expired, Duration.ofHours(1L));
        log.debug("脚本缓存失效时间 - {}", this.expired);

        this.enableDebug = ObjectUtil.defaultIfNull(this.enableDebug, Boolean.FALSE);
        log.debug("是否开启调试模式 - {}", this.enableDebug);

        this.enableMethodInvoke = ObjectUtil.defaultIfNull(this.enableMethodInvoke, Boolean.TRUE);
        log.debug("是否开启方法访问 - {}", this.enableMethodInvoke);

        this.serviceFinder = ObjectUtil.defaultIfNull(this.serviceFinder, new DefaultServiceFinder());
        log.debug("服务发现者实现类 - {}", this.serviceFinder.getClass().getName());

        this.functionMissing = ObjectUtil.defaultIfNull(this.functionMissing, new DefaultFunctionMissing(this.enableMethodInvoke));
        log.debug("函数兜底器实现类 - {}", this.functionMissing.getClass().getName());

        this.definable = ObjectUtil.defaultIfNull(this.definable, new DefaultVariableDefinable());
        log.debug("变量定义器实现类 - {}", this.definable.getClass().getName());

        this.exceptionHandler = ObjectUtil.defaultIfNull(this.exceptionHandler, new DefaultExceptionHandler());
        log.debug("异常处理器实现类 - {}", this.exceptionHandler.getClass().getName());

        this.packageNameList.forEach(name -> log.debug("函数库扫描包路径 - {}", name));

        this.loaders.forEach(loader -> log.debug("函数加载器实现类 - {}", loader.getClass().getName()));

        this.enhancers.forEach(enhancer -> log.debug("函数增强器实现类 - {}", enhancer.getClass().getName()));

        this.engine = new ScriptEngine(this);

        this.compiler = ObjectUtil.defaultIfNull(this.compiler, new DefaultScriptCompiler(this));
        log.debug("脚本编译器实现类 - {}", this.compiler.getClass().getName());

        // 根据函数加载器依次加载函数库
        loaders.stream()
                .flatMap(loader -> loader.load(this).stream())
                .forEach(function -> {
                    // 根据函数过滤器决定是否注册函数
                    BoolWrapper allowed = new BoolWrapper(true);
                    filters.forEach(filter -> allowed.and(filter.allowedRegister(function)));
                    if (allowed.get()) {
                        functionHolder.register(function);
                    } else {
                        log.debug("取消注册 - [{}]", function.name());
                    }
                });

        // 函数库全部加载完成后，由函数增强器列表依次增强函数
        functionHolder.onVisit(function -> enhancers.forEach(enhancer -> enhancer.enhance(function)));

        // 重置刷新标记
        this.refreshed = Boolean.TRUE;
    }
}
