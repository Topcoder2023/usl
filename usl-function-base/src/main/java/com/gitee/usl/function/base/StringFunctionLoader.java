package com.gitee.usl.function.base;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.FunctionLoader;
import com.gitee.usl.kernel.configure.EngineConfig;
import com.gitee.usl.kernel.engine.Function;
import com.google.auto.service.AutoService;
import com.gitee.usl.grammar.type.USLFunction;

import java.util.List;

/**
 * @author hongda.li
 */
@AutoService(FunctionLoader.class)
public class StringFunctionLoader implements FunctionLoader {
    /**
     * 字符串类函数的统一前缀
     */
    public static final String STRING_FUNCTION_PREFIX = "string_";

    @Override
    public List<USLFunction> load(EngineConfig configuration) {
        return Function.newBuilder()
                .runner(configuration.getConfiguration().getRunner())
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
