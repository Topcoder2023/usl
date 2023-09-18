package com.gitee.usl.app.plugin;

import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.UslInvocation;
import com.gitee.usl.kernel.engine.UslFunctionDefinition;
import com.gitee.usl.kernel.engine.UslFunctionSession;
import com.gitee.usl.kernel.plugin.UslBeginPlugin;

/**
 * @author hongda.li
 */
public class ParameterBinderPlugin implements UslBeginPlugin {
    @Override
    public void onBegin(UslFunctionSession session) {
        UslFunctionDefinition definition = session.getDefinition();

        UslInvocation<?> invocation = definition.getInvocation();

        if (invocation.method().getParameterCount() == NumberConstant.ZERO) {
            return;
        }

        Object[] args = null;

        UslInvocation<?> from = UslInvocation.from(invocation, args);

        definition.setInvocation(from);
    }
}
