package com.manda.usl.kernel.view;

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
     *
     * @return 共享会话实例
     */
    public static FunctionSession getSession() {
        return LOCAL.get();
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
