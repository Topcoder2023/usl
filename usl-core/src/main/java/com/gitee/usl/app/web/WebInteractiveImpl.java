package com.gitee.usl.app.web;

import cn.hutool.core.net.NetUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.configure.WebServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.server.HttpBootstrap;

/**
 * 采用内嵌式 WEB-SERVER 实现
 * 考虑到轻量级依赖，此处未选择 Tomcat、Undertow、Jetty 等，而是选择了 Smart-http-server
 *
 * @author hongda.li
 */
public class WebInteractiveImpl implements WebInteractive {
    private final HttpBootstrap bootstrap;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WebInteractiveImpl() {
        bootstrap = new HttpBootstrap();
    }

    @Override
    public void open(USLRunner runner) {
        WebServerConfiguration config = runner.configuration().configWebServer();
        String host = NetUtil.getLocalhostStr();

        bootstrap.configuration()
                .host(host)
                .bannerEnabled(false)
                .debug(config.isDebug())
                .serverName(config.getName());

        bootstrap.httpHandler(new ScriptRequestHandler(runner));
        bootstrap.setPort(config.getPort());
        bootstrap.start();

        logger.info("Web-Interactive start success - [{}://{}:{}]", "http", host, config.getPort());
    }
}
