package com.gitee.usl.kernel.plugin;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.utils.EnabledLogger;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.FailurePlugin;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
@Description("日志插件")
public class LoggerPlugin implements BeginPlugin, SuccessPlugin, FailurePlugin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onBegin(FunctionSession session) {
        String name = session.getDefinition().getName();
        Supplier<Object[]> supplier = () -> new Object[]{name, format(session.getEnv(), session.getObjects())};
        EnabledLogger.debug(logger, "参数列表 - [{}] : [{}]", supplier);
    }

    @Override
    public void onFailure(FunctionSession session) {
        String name = session.getDefinition().getName();
        Supplier<Object[]> supplier = () -> new Object[]{name, session.getException().getMessage()};
        EnabledLogger.warn(logger, "执行失败 - [{}] : [{}]", supplier);
    }

    @Override
    public void onSuccess(FunctionSession session) {
        String name = session.getDefinition().getName();
        Supplier<Object[]> supplier = () -> new Object[]{name, session.getResult()};
        EnabledLogger.debug(logger, "执行成功 - [{}] : [{}]", supplier);
    }

    @Description("格式化参数")
    protected String format(Env env, AviatorObject[] objects) {
        return Arrays.stream(objects)
                .map(item -> String.valueOf(item.getValue(env)))
                .collect(Collectors.joining(CharPool.COMMA + CharSequenceUtil.SPACE));
    }

}
