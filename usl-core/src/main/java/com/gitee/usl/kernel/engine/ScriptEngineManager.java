package com.gitee.usl.kernel.engine;

import cn.hutool.core.thread.ThreadUtil;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Notes;
import com.gitee.usl.infra.constant.ModuleConstant;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.UslExecuteException;
import com.gitee.usl.infra.utils.ScriptCompileHelper;
import com.gitee.usl.kernel.cache.Cache;
import com.gitee.usl.kernel.configure.CacheConfiguration;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.gitee.usl.kernel.configure.QueueConfiguration;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.*;

import java.util.concurrent.TimeUnit;

/**
 * USL 脚本引擎
 * SHA512 —— 唯一缓存键生成
 * SpinLock —— 异步加载编译结果
 * W-TinyLFU —— 缓存淘汰算法
 * Token-Bucket —— 令牌桶限流组件
 * Timing-Wheel —— 时间轮任务调度
 * Count–Min Sketch —— 编译缓存计数算法
 * Non-blocking Synchronization —— 无锁化编程
 * <br/>
 *
 * @author hongda.li
 */
@Notes(value = "脚本引擎初始化器",
        belongs = ModuleConstant.USL_CORE,
        viewUrl = "https://gitee.com/yixi-dlmu/usl/raw/master/usl-core/src/main/java/com/gitee/usl/kernel/engine/ScriptEngineManager.java")
@AutoService(Initializer.class)
public final class ScriptEngineManager implements Initializer {
    private AviatorEvaluatorInstance instance;
    private Configuration configuration;
    private CacheConfiguration cacheConfiguration;
    private QueueConfiguration queueConfiguration;

    @Override
    public void doInit(Configuration uslConfiguration) {
        this.configuration = uslConfiguration;
        this.cacheConfiguration = uslConfiguration.configCache();
        this.queueConfiguration = uslConfiguration.configQueue();

        EngineConfiguration configEngine = uslConfiguration.configEngine();

        this.instance = AviatorEvaluator.newInstance();
        this.instance.removeFunctionLoader(ClassPathConfigFunctionLoader.getInstance());
        this.instance.addFunctionLoader(name -> configEngine.functionHolder().search(name));

        configEngine.setScriptEngineManager(this);
    }

    /**
     * 编译并执行脚本
     *
     * @param param 脚本执行参数
     * @param <T>   脚本执行泛型
     * @return 脚本执行结果
     */
    @SuppressWarnings({"unchecked", "ReassignedVariable"})
    public <T> Result<T> run(Param param) {
        // 使用SHA512摘要算法生成唯一Key
        String key = ScriptCompileHelper.generateKey(param.getScript());
        Cache cache = this.cacheConfiguration.cacheManager().cache();

        Expression expression;

        // 1.开启 USL 脚本编译缓存
        if (param.isCached()) {
            // 1.1.直接从缓存中获取编译结果
            expression = cache.select(key);

            // 1.2.缓存中不存在
            if (expression == null) {
                // 1.2.1.编译脚本
                this.compile(param.getScript());

                // 1.2.2.获取编译结果
                expression = this.getWithSpin(key);
            }
        }

        // 2.未开启 USL 脚本编译缓存
        else {
            // 2.1.编译脚本
            this.compile(param.getScript());

            // 2.2.获取编译结果
            expression = this.getWithSpin(key);

            // 2.3.移除缓存
            cache.remove(key);
        }

        // 校验编译结果是否为有效结果
        if (ScriptCompileHelper.isEmpty(expression)) {
            return Result.failure(ResultCode.COMPILE_FAILURE);
        }

        try {
            return Result.success((T) expression.execute(param.getContext()));
        } catch (UslExecuteException exception) {
            return Result.failure(exception.getResultCode(), exception.getMessage());
        }
    }

    /**
     * 将表达式封装为编译事件通过编译队列的生产者发布出去
     *
     * @param content 脚本的内容
     */
    private void compile(String content) {
        queueConfiguration.compileQueueInitializer()
                .producer()
                .produce(content, this.configuration);
    }

    /**
     * 尝试通过缓存键从缓存中获取
     * 若缓存中不存在则尝试自旋 CPU 获取
     *
     * @param key 缓存键
     * @return 缓存中编译后的表达式
     */
    @SuppressWarnings("ReassignedVariable")
    private Expression getWithSpin(String key) {
        Cache cache = this.cacheConfiguration.cacheManager().cache();
        Expression expression = null;

        // CPU 自旋阻塞获取编译后的表达式
        while (expression == null) {
            expression = cache.select(key);

            if (expression == null) {
                // 减少CPU自旋的次数
                // 在性能和资源两者直接平衡
                ThreadUtil.sleep(NumberConstant.ONE, TimeUnit.NANOSECONDS);
            }
        }

        return expression;
    }

    /**
     * 获取 Aviator 引擎实例
     *
     * @return Aviator 引擎实例
     */
    public AviatorEvaluatorInstance getInstance() {
        return instance;
    }
}
