package com.gitee.usl.domain;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.WebRoute;
import com.gitee.usl.api.annotation.Accessible;
import com.gitee.usl.api.impl.DefaultHttpHandler;
import com.gitee.usl.infra.structure.SharedSession;
import com.gitee.usl.infra.structure.StringMap;
import lombok.Getter;
import org.smartboot.http.server.HttpBootstrap;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author hongda.li
 */
public class HttpServer {
    /**
     * 内置变量名称
     */
    private static final String REQUEST = "request";
    /**
     * 内置变量名称
     */
    private static final String RESPONSE = "response";
    /**
     * 端口号
     */
    @Getter
    private final int port;
    /**
     * 本机地址
     */
    @Getter
    private final String host;
    /**
     * 服务器名称
     */
    @Getter
    private final String serverName;
    /**
     * 服务器代理
     */
    private final HttpBootstrap proxy;
    /**
     * 路由映射
     */
    @Getter
    private final StringMap<WebRoute> routeMapping = new StringMap<>();

    public HttpServer(int port) {
        this(port, NetUtil.getLocalhostStr());
    }

    public HttpServer(int port, String host) {
        this.host = host;
        this.port = port;
        this.proxy = new HttpBootstrap();
        this.serverName = this.generateName();
        this.proxy.configuration()
                .host(host)
                .debug(false)
                .bannerEnabled(false)
                .serverName(serverName);
        this.proxy.setPort(port);
    }

    @Accessible
    public HttpServer start() {
        this.proxy.httpHandler(new DefaultHttpHandler(routeMapping));
        this.proxy.start();
        return this;
    }

    public HttpServer addRoute(HttpServer server,
                               String path,
                               String resource,
                               Consumer<WebRoute> consumer) {
        USLRunner runner = SharedSession.getSession().getDefinition().getRunner();
        StringMap<WebRoute> handlerMap = server.getRouteMapping();
        WebRoute route = new WebRoute() {
            @Override
            public Boolean doHandle(HttpRequest request, HttpResponse response) {
                ExecutableParam param = new ExecutableParam(runner, new ResourceParam(resource));
                HttpRequestWrapper requestWrapper = new HttpRequestWrapper();
                requestWrapper.set(request);
                param.addContext(REQUEST, requestWrapper);
                HttpResponseWrapper responseWrapper = new HttpResponseWrapper();
                responseWrapper.set(response);
                param.addContext(RESPONSE, responseWrapper);
                return Optional.ofNullable(param.execute())
                        .map(res -> Convert.toBool(res, Boolean.TRUE))
                        .orElse(Boolean.TRUE);
            }
        };
        consumer.accept(route);
        handlerMap.put(path, route);
        return server;
    }

    private String generateName() {
        return "[USL-HTTP-Server-(host:port)]"
                .replace("host", this.host)
                .replace("port", String.valueOf(this.port));
    }
}
