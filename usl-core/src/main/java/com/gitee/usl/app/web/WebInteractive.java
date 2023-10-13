package com.gitee.usl.app.web;

import cn.hutool.core.net.NetUtil;
import com.gitee.usl.UslRunner;
import com.gitee.usl.app.Interactive;
import com.gitee.usl.kernel.configure.WebServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.server.HttpBootstrap;

/**
 * @author hongda.li
 */
public class WebInteractive implements Interactive {
    private final HttpBootstrap bootstrap;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WebInteractive() {
        bootstrap = new HttpBootstrap();
    }

    @Override
    public void open(UslRunner runner) {
        WebServerConfiguration config = runner.configuration().configWebServer();
        String host = NetUtil.getLocalhostStr();

        bootstrap.configuration()
                .host(host)
                .bannerEnabled(false)
                .debug(config.isDebug())
                .serverName(config.getName());

        bootstrap.httpHandler(new RequestDispatcher(runner));
        bootstrap.setPort(config.getPort());
        bootstrap.start();

        logger.info("Web-Interactive start success - [{}://{}:{}]", "http", host, config.getPort());
    }
}
