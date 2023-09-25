package com.gitee.usl.app.plugin;

import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.kernel.plugin.UslBeginPlugin;
import com.gitee.usl.kernel.plugin.UslFailurePlugin;
import com.gitee.usl.kernel.plugin.UslSuccessPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongda.li
 */
public class LoggerPlugin implements UslBeginPlugin, UslSuccessPlugin, UslFailurePlugin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onBegin(FunctionSession session) {

    }

    @Override
    public void onFailure(FunctionSession session) {
        logger.warn("USL function execute failure - [{}]", session.getException().getMessage());
    }

    @Override
    public void onSuccess(FunctionSession session) {
        logger.info("USL function execute success - [{}]", session.getResult());
    }
}
