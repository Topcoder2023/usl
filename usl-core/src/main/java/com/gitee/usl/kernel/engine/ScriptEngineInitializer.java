package com.gitee.usl.kernel.engine;

import cn.hutool.core.thread.ThreadUtil;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.infra.utils.MethodInvokerOnMissing;
import com.gitee.usl.infra.structure.StringConsumer;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.gitee.usl.infra.structure.wrapper.ObjectWrapper;
import com.gitee.usl.infra.utils.ScriptCompileHelper;
import com.gitee.usl.kernel.cache.CacheValue;
import com.gitee.usl.kernel.cache.ExpressionCache;
import com.gitee.usl.kernel.configure.EngineConfig;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@Slf4j
@Description({"USL脚本引擎",
        "SHA512 —— 唯一缓存键生成",
        "SpinLock —— 异步加载编译结果",
        "W-TinyLFU —— 缓存淘汰算法",
        "Token-Bucket —— 令牌桶限流组件",
        "Timing-Wheel —— 时间轮任务调度",
        "Count–Min Sketch —— 编译缓存计数算法",
        "Non-blocking Synchronization —— 无锁化编程"})
@Getter
@AutoService(Initializer.class)
public final class ScriptEngineInitializer implements Initializer {

    @Description("编译事件发布函数")
    private StringConsumer publisher;

    @Description("缓存实例")
    private ExpressionCache expressionCache;

    @Description("Aviator引擎实例")
    private AviatorEvaluatorInstance instance;

    @Override
    public void doInit(Configuration configuration) {
        this.expressionCache = configuration.getCacheConfig().getCacheInitializer().getCache();

        EngineConfig configEngine = configuration.getEngineConfig();

        this.instance = new AviatorEvaluatorInstance();
        this.instance.setFunctionLoader(name -> configEngine.getFunctionHolder().search(name));
        this.instance.setFunctionMissing(new MethodInvokerOnMissing()
                .setFunctionMissing(configEngine.getFunctionMissing())
                .setEnabled(Boolean.TRUE.equals(configEngine.getEnableMethodInvoke())));

        configEngine.setEngineInitializer(this);

        this.publisher = content -> configuration.getQueueConfig()
                .getQueueInitializer()
                .getProducer()
                .produce(content, configuration);
    }

    @Description("编译并执行脚本")
    @SuppressWarnings({"unchecked"})
    public <T> Result<T> run(Param param) {

        @Description("脚本内容")
        String script = param.getScript();

        @Description("缓存启用标识")
        boolean enableCache = param.isCached();

        @Description("表达式缓存键")
        String key = ScriptCompileHelper.generateKey(script);

        @Description("自旋次数阈值")
        IntWrapper count = new IntWrapper(NumberConstant.NORMAL_MAX_SIZE);

        @Description("表达式编译值")
        ObjectWrapper<CacheValue> expression = new ObjectWrapper<>(expressionCache.select(key));

        if (expression.isEmpty()) {
            this.publisher.accept(script);

            while (expression.isEmpty() && !count.isZero()) {
                ThreadUtil.sleep(NumberConstant.ONE, TimeUnit.NANOSECONDS);
                expression.set(expressionCache.select(key));
                count.decrement();
            }

            if (enableCache) {
                expressionCache.remove(key);
            }
        }

        if (ScriptCompileHelper.isEmpty(expression.get().getExpression())) {
            return Result.failure(ResultCode.COMPILE_FAILURE);
        }

        try {
            return Result.success((T) expression.get()
                    .getExpression()
                    .execute(param.addContext(expression.get().getInitEnv()).getContext()));
        } catch (USLExecuteException uee) {
            log.warn("USL执行出现错误", uee);
            return Result.failure(uee.getResultCode(), uee.getMessage());
        }
    }
}
