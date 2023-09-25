package com.gitee.usl.app.plugin;

import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.kernel.plugin.UslBeginPlugin;

/**
 * @author hongda.li
 */
public class ParameterBinderPlugin implements UslBeginPlugin {
    @Override
    public void onBegin(FunctionSession session) {
        FunctionDefinition definition = session.getDefinition();

        Invocation<?> invocation = definition.getInvocation();

        if (invocation.method().getParameterCount() == NumberConstant.ZERO) {
            return;
        }

        Object[] args = null;

        Invocation<?> from = Invocation.from(invocation, args);

        definition.setInvocation(from);
    }
}
