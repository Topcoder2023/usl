package com.gitee.usl.app.plugin;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.kernel.plugin.UslBeginPlugin;
import com.gitee.usl.kernel.plugin.UslFailurePlugin;
import com.gitee.usl.kernel.plugin.UslSuccessPlugin;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
public class LoggerPlugin implements UslBeginPlugin, UslSuccessPlugin, UslFailurePlugin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onBegin(FunctionSession session) {
        if (logger.isInfoEnabled()) {
            String name = session.getDefinition().getName();
            logger.info("USL function execute params - [{}] : [{}]", name, this.format(session.getEnv(), session.getObjects()));
        }
    }

    @Override
    public void onFailure(FunctionSession session) {
        String name = session.getDefinition().getName();
        logger.warn("USL function execute errors - [{}] : [{}]", name, session.getException().getMessage());
    }

    @Override
    public void onSuccess(FunctionSession session) {
        String name = session.getDefinition().getName();
        logger.info("USL function execute return - [{}] : [{}]", name, session.getResult());
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
