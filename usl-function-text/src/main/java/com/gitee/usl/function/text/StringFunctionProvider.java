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
    private static final String STRING_FUNCTION_PREFIX = "string.";

    @Override
    public List<AviatorFunction> provide(EngineConfiguration configuration) {
        return Function.newBuilder()
                .clazz(CharSequenceUtil.class)
                .mapping(methodName -> STRING_FUNCTION_PREFIX + methodName)
                .method("isEmpty")
                .next(false)
                .method("isBlank")
                .next(false)
                .method("isNotBlank")
                .next(false)
                .method("isNotEmpty")
                .next(false)
                .method("hasBlank")
                .next(false)
                .method("isAllBlank")
                .next(false)
                .method("hasEmpty")
                .next(false)
                .method("isAllEmpty")
                .next(false)
                .method("isAllNotEmpty")
                .next(false)
                .method("isAllNotBlank")
                .next(false)
                .method("emptyIfNull")
                .next(false)
                .method("nullToEmpty")
                .next(false)
                .method("nullToDefault")
                .next(false)
                .method("emptyToDefault")
                .next(false)
                .method("blankToDefault")
                .next(false)
                .method("emptyToNull")
                .buildAll();
    }
}
