package com.gitee.usl.app.web;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.net.NetUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.WebServerConfiguration;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.server.HttpBootstrap;
import org.smartboot.http.server.HttpServerHandler;
import org.smartboot.http.server.handler.HttpRouteHandler;

import java.util.List;

/**
 * B-S架构
 * 接收来自外部的请求
 * 采用内嵌式 WEB-SERVER 实现
 * 考虑到轻量级依赖，此处未选择 Tomcat、Undertow、Jetty 等，而是选择了 Smart-http-server
 *
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE)
@AutoService(WebInteractive.class)
public class WebInteractiveImpl extends HttpServerHandler implements WebInteractive {
    private final HttpBootstrap bootstrap;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WebInteractiveImpl() {
        bootstrap = new HttpBootstrap();
    }

    @Override
    public void open(USLRunner runner) {
        Singleton.put(StringConstant.RUNNER_NAME, runner);

        WebServerConfiguration config = runner.configuration().configWebServer();
        String host = NetUtil.getLocalhostStr();

        bootstrap.configuration()
                .host(host)
                .bannerEnabled(false)
                .debug(config.isDebug())
                .serverName(config.getName());

        HttpRouteHandler routeHandler = new HttpRouteHandler();
        List<AbstractWebHandler> handlers = ServiceSearcher.searchAll(AbstractWebHandler.class);
        handlers.forEach(handler -> routeHandler.route(handler.getRoute(), handler));

        bootstrap.httpHandler(routeHandler);
        bootstrap.setPort(config.getPort());
        bootstrap.start();

        logger.info("Web-Interactive start success - [{}://{}:{}]", "http", host, config.getPort());
    }
}
