package com.gitee.usl.infra.structure;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.kernel.engine.FunctionSession;

import java.util.Objects;

/**
 * 共享会话
 * 使用 ThreadLocal 结构存储当前线程的会话信息
 *
 * @author hongda.li
 */
public class SharedSession {
    private static final ThreadLocal<FunctionSession> LOCAL = new ThreadLocal<>();

    private SharedSession() {
    }

    /**
     * 获取当前线程下的共享会话
     * 返回值不为空
     * 若共享会话为空，可能是未注入共享会话插件增强器 {@link com.gitee.usl.kernel.enhancer.SharedPluginEnhancer}
     *
     * @return 共享会话实例
     */
    public static FunctionSession getSession() {
        FunctionSession session = LOCAL.get();
        Assert.notNull(session, () -> new USLExecuteException(ResultCode.EMPTY_SHARED_SESSION));
        return session;
    }

    /**
     * 设置当前线程下的共享会话
     *
     * @param session 共享会话实例
     */
    public static void setSession(FunctionSession session) {
        Objects.requireNonNull(session);
        LOCAL.set(session);
    }

    /**
     * 清空当前线程下的共享会话
     */
    public static void clear() {
        LOCAL.remove();
    }

}
