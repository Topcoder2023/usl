package com.gitee.usl.function.text;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.FunctionProvider;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.gitee.usl.kernel.engine.Function;
import com.gitee.usl.plugin.impl.LoggerPlugin;
import com.gitee.usl.plugin.impl.ParameterBinderPlugin;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.util.List;

/**
 * @author hongda.li
 */
@AutoService(FunctionProvider.class)
public class StringFunctionProvider implements FunctionProvider {
    @Override
    public List<AviatorFunction> provide(EngineConfiguration configuration) {
        return Function.newBuilder()
                .clazz(CharSequenceUtil.class)
                .method("isEmpty")
                .names("str.isEmpty")
                .plugin(Singleton.get(LoggerPlugin.class))
                .plugin(Singleton.get(ParameterBinderPlugin.class))
                // 复用上述配置
                .next(false)
                .method("isBlank")
                .names("str.isBlank")
                .buildAll();
    }
}
