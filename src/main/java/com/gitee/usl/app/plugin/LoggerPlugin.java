package com.gitee.usl.app.plugin;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.kernel.plugin.BeginPlugin;
import com.gitee.usl.kernel.plugin.FailurePlugin;
import com.gitee.usl.kernel.plugin.SuccessPlugin;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.gitee.usl.infra.utils.EnabledLogger.info;
import static com.gitee.usl.infra.utils.EnabledLogger.warn;

/**
 * @author hongda.li
 */
public class LoggerPlugin implements BeginPlugin, SuccessPlugin, FailurePlugin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onBegin(FunctionSession session) {
        String name = session.definition().name();
        Supplier<Object[]> supplier = () -> new Object[]{name, format(session.env(), session.objects())};
        info(logger, "USL function execute params - [{}] : [{}]", supplier);
    }

    @Override
    public void onFailure(FunctionSession session) {
        String name = session.definition().name();
        Supplier<Object[]> supplier = () -> new Object[]{name, session.exception().getMessage()};
        warn(logger, "USL function execute errors - [{}] : [{}]", supplier);
    }

    @Override
    public void onSuccess(FunctionSession session) {
        String name = session.definition().name();
        Supplier<Object[]> supplier = () -> new Object[]{name, session.result()};
        info(logger, "USL function execute return - [{}] : [{}]", supplier);
    }

    /**
     * 格式化参数
     *
     * @param env     上下文环境
     * @param objects 原始参数
     * @return 参数描述
     */
    protected String format(Env env, AviatorObject[] objects) {
        return Arrays.stream(objects)
                .map(item -> item.desc(env))
                .collect(Collectors.joining(CharPool.COMMA + CharSequenceUtil.SPACE));
    }
}
