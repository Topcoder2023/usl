package com.gitee.usl.resource;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.WebInteractive;
import com.gitee.usl.domain.HttpServerWrapper;
import com.gitee.usl.domain.ResourceParam;
import com.gitee.usl.resource.handler.StaticRequestHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * B-S架构
 * 接收来自外部的请求
 * 采用内嵌式 WEB-SERVER 实现
 * 考虑到轻量级依赖，此处未选择 Tomcat、Undertow、Jetty 等，而是选择了 Smart-http-server
 *
 * @author hongda.li
 */
@Slf4j
public class WebInteractiveImpl implements WebInteractive {

    public static final String WEB_PORT = "Web_Port";

    public static final String WEB_SERVER = "server";

    @Override
    public void open(USLRunner runner) {
        int port = runner.getConfiguration().getInt(WEB_PORT, 10086);
        HttpServerWrapper server = new HttpServerWrapper(port, runner);
        server.getRouteMapping().put(StaticRequestHandler.PATH, new StaticRequestHandler());
        runner.run(new ResourceParam("route/main.js").addContext(WEB_SERVER, server));
        log.debug("登录路径 - [{}]", "/public/login");
        server.start();
    }
}
