package com.gitee.usl.kernel.engine;

import cn.hutool.core.thread.ThreadUtil;
import com.gitee.usl.api.UslInitializer;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.UslExecuteException;
import com.gitee.usl.kernel.cache.UslCache;
import com.gitee.usl.kernel.configure.CacheConfiguration;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.gitee.usl.kernel.configure.QueueConfiguration;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.domain.UslParam;
import com.gitee.usl.kernel.domain.UslResult;
import com.gitee.usl.kernel.queue.CompileGeneratorConsumer;
import com.gitee.usl.kernel.queue.CompileQueueManager;
import com.google.common.hash.Hashing;
import com.googlecode.aviator.*;

import java.nio.charset.StandardCharsets;
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
@SuppressWarnings("UnstableApiUsage")
public final class UslScriptEngineManager implements UslInitializer {
    private AviatorEvaluatorInstance instance;
    private UslConfiguration uslConfiguration;
    private CacheConfiguration cacheConfiguration;
    private QueueConfiguration queueConfiguration;

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        this.uslConfiguration = uslConfiguration;
        this.cacheConfiguration = uslConfiguration.getCacheConfiguration();
        this.queueConfiguration = uslConfiguration.getQueueConfiguration();

        EngineConfiguration configuration = uslConfiguration.getEngineConfiguration();

        this.instance = AviatorEvaluator.newInstance();
        this.instance.removeFunctionLoader(ClassPathConfigFunctionLoader.getInstance());
        this.instance.addFunctionLoader(name -> configuration.getFunctionHolder().search(name));

        configuration.setScriptEngineManager(this);
    }

    /**
     * 编译并执行脚本
     *
     * @param uslParam 脚本执行参数
     * @param <T>      脚本执行泛型
     * @return 脚本执行结果
     */
    @SuppressWarnings({"unchecked", "ReassignedVariable"})
    public <T> UslResult<T> run(UslParam uslParam) {
        // 使用SHA512摘要算法生成唯一Key
        String key = UslScriptEngineManager.generateKey(uslParam.getContent());

        Expression expression;

        // 1.开启 USL 脚本编译缓存
        if (uslParam.isCached()) {
            // 1.1.直接从缓存中获取编译结果
            expression = this.cacheConfiguration.getUslCache().select(key);

            // 1.2.缓存中不存在
            if (expression == null) {
                // 1.2.1.编译脚本
                this.compile(uslParam.getContent());

                // 1.2.2.获取编译结果
                expression = this.getWithSpin(key);
            }
        }

        // 2.未开启 USL 脚本编译缓存
        else {
            // 2.1.编译脚本
            this.compile(uslParam.getContent());

            // 2.2.获取编译结果
            expression = this.getWithSpin(key);

            // 2.3.移除缓存
            this.cacheConfiguration.getUslCache().remove(key);
        }

        // 校验编译结果是否为有效结果
        if (this.isInvalid(expression)) {
            return UslResult.failure(ResultCode.COMPILE_FAILURE);
        }

        try {
            return UslResult.success((T) expression.execute(uslParam.getContext()));
        } catch (UslExecuteException executeException) {
            return UslResult.failure(executeException.getResultCode(), executeException.getMessage());
        }
    }

    /**
     * 将表达式封装为编译事件通过编译队列的生产者发布出去
     *
     * @param content 脚本的内容
     */
    private void compile(String content) {
        CompileQueueManager queueManager = queueConfiguration.getCompileQueueManager();
        queueManager.getProducer().produce(content, this.uslConfiguration);
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
        UslCache uslCache = this.cacheConfiguration.getUslCache();
        Expression expression = null;

        // CPU 自旋阻塞获取编译后的表达式
        while (expression == null) {
            expression = uslCache.select(key);

            if (expression == null) {
                // 减少CPU自旋的次数
                // 在性能和资源两者直接平衡
                ThreadUtil.sleep(NumberConstant.ONE, TimeUnit.NANOSECONDS);
            }
        }

        return expression;
    }

    /**
     * 是否为无效编译结果
     *
     * @param expression 无效编译结果
     * @return 判断结果
     */
    private boolean isInvalid(Expression expression) {
        return CompileGeneratorConsumer.EMPTY_PLACE_HOLDER.equals(expression);
    }

    /**
     * 获取 Aviator 引擎实例
     *
     * @return Aviator 引擎实例
     */
    public AviatorEvaluatorInstance getInstance() {
        return instance;
    }

    /**
     * 生成脚本内容对应的唯一缓存键
     * 采用 SHA512 摘要算法
     *
     * @param content 脚本内容
     * @return 唯一缓存键
     */
    public static String generateKey(String content) {
        return Hashing.sha512()
                .newHasher()
                .putString(content, StandardCharsets.UTF_8)
                .hash()
                .toString();
    }
}
