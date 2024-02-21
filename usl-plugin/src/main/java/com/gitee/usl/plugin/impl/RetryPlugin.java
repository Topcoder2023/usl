package com.gitee.usl.plugin.impl;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.plugin.api.RetryBuilderFactory;
import com.gitee.usl.plugin.annotation.Retryable;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.github.rholder.retry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * 重试插件
 *
 * @author hongda.li
 */
public class RetryPlugin implements BeginPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryPlugin.class);
    private final Retryer<Object> retryer;

    public RetryPlugin(Retryer<Object> retryer) {
        this.retryer = retryer;
    }

    @Override
    public void onBegin(FunctionSession session) {
        try {
            session.setResult(retryer.call(() -> session.getHandler().apply(session)));
        } catch (ExecutionException | RetryException e) {
            throw new USLExecuteException(e);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static final class DefaultRetryBuilderFactory implements RetryBuilderFactory {
        private final Retryable retryable;

        public DefaultRetryBuilderFactory(Retryable retryable) {
            Assert.notNull(retryable, "@Retryable annotation can not be null");
            this.retryable = retryable;
        }

        @Override
        public Retryer<Object> create() {
            return RetryerBuilder.newBuilder()
                    // 配置重试条件
                    .retryIfException()
                    // 配置重试监听器
                    .withRetryListener(new RetryListener() {
                        @Override
                        public <V> void onRetry(Attempt<V> attempt) {
                            LOGGER.warn("An exception occurred during execution, try again... - [{}, {}]",
                                    attempt.getAttemptNumber(), attempt.getExceptionCause().getMessage());
                        }
                    })
                    // 默认采用等待策略
                    .withBlockStrategy(BlockStrategies.threadSleepStrategy())
                    // 重试时间限制策略
                    .withAttemptTimeLimiter(AttemptTimeLimiters.noTimeLimit())
                    // 重试时间间隔策略
                    .withWaitStrategy(retryable.retryTime() <= NumberConstant.ZERO
                            ? WaitStrategies.noWait()
                            : WaitStrategies.fixedWait(retryable.retryTime(), retryable.retryUnit()))
                    // 重试的总次数策略
                    .withStopStrategy(StopStrategies.stopAfterAttempt(retryable.times()))
                    // 构造重试器
                    .build();
        }
    }
}
