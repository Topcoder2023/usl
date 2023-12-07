package com.gitee.usl.function.text;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.FunctionProvider;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.gitee.usl.kernel.engine.Function;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.util.List;

/**
 * @author hongda.li
 */
@AutoService(FunctionProvider.class)
public class StringFunctionProvider implements FunctionProvider {
    /**
     * 字符串类函数的统一前缀
     */
    public static final String STRING_FUNCTION_PREFIX = "string_";

    @Override
    public List<AviatorFunction> provide(EngineConfiguration configuration) {
        return Function.newBuilder()
                .runner(configuration.finish().getRunner())
                .clazz(CharSequenceUtil.class)
                .mapping(methodName -> STRING_FUNCTION_PREFIX + methodName)
                .method("isEmpty")
                .next()
                .method("isBlank")
                .next()
                .method("isNotBlank")
                .next()
                .method("isNotEmpty")
                .next()
                .method("hasBlank")
                .next()
                .method("isAllBlank")
                .next()
                .method("hasEmpty")
                .next()
                .method("isAllEmpty")
                .next()
                .method("isAllNotEmpty")
                .next()
                .method("isAllNotBlank")
                .next()
                .method("emptyIfNull")
                .next()
                .method("nullToEmpty")
                .next()
                .method("nullToDefault")
                .next()
                .method("emptyToDefault")
                .next()
                .method("blankToDefault")
                .next()
                .method("emptyToNull")
                .buildAll();
    }
}
