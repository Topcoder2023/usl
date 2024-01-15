package com.gitee.usl.kernel.plugin;

import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.grammar.runtime.FunctionArgument;
import com.gitee.usl.grammar.runtime.function.FunctionUtils;

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
