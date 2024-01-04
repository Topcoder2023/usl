package com.gitee.usl.kernel.plugin;

import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.googlecode.aviator.runtime.FunctionArgument;
import com.googlecode.aviator.runtime.function.FunctionUtils;

import java.util.List;

/**
 * @author hongda.li
 */
public class LocationPlugin implements BeginPlugin {
    @Override
    public void onBegin(FunctionSession session) {
        List<FunctionArgument> arguments = FunctionUtils.getFunctionArguments(session.getEnv());
    }
}
