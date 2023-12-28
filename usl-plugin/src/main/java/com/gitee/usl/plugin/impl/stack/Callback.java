package com.gitee.usl.plugin.impl.stack;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.engine.FunctionSession;

import java.util.Deque;

/**
 * @author hongda.li
 */
@Description("回调函数接口")
public interface Callback {

    @Description("回调方法")
    void onPush(Deque<FunctionSession> deque);

}
