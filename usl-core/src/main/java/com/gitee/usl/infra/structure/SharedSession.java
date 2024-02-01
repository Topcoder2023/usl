package com.gitee.usl.infra.structure;

import com.gitee.usl.kernel.engine.FunctionSession;

import java.util.Optional;

/**
 * 共享会话
 * 使用 ThreadLocal 结构存储当前线程的会话信息
 *
 * @author hongda.li
 */
public class SharedSession {
    private static final FunctionSession EMPTY_SESSION = new FunctionSession(null, null, null);
    private static final ThreadLocal<FunctionSession> LOCAL = new ThreadLocal<>();

    private SharedSession() {
    }

    public static FunctionSession empty() {
        return EMPTY_SESSION;
    }

    public static FunctionSession getSession() {
        return Optional.ofNullable(LOCAL.get()).orElse(empty());
    }

    public static void setSession(FunctionSession session) {
        LOCAL.set(session);
    }

    public static void clear() {
        LOCAL.remove();
    }

}
