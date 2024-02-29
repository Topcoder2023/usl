package com.gitee.usl.domain;

import cn.hutool.core.net.NetUtil;
import com.gitee.usl.api.WebRoute;
import com.gitee.usl.api.impl.DefaultHttpHandler;
import com.gitee.usl.infra.structure.StringMap;
import lombok.Data;
import org.smartboot.http.server.HttpBootstrap;

/**
 * @author hongda.li
 */
@Data
public class HttpServer {
    /**
     * 端口号
     */
    private final int port;
    /**
     * 本机地址
     */
    private final String host;
    /**
     * 服务器名称
     */
    private final String serverName;
    /**
     * 服务器代理
     */
    private final HttpBootstrap proxy;
    /**
     * 路由映射
     */
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

    public HttpServer start() {
        this.proxy.httpHandler(new DefaultHttpHandler(routeMapping));
        this.proxy.start();
        return this;
    }

    private String generateName() {
        return "[USL-HTTP-Server-(host:port)]"
                .replace("host", this.host)
                .replace("port", String.valueOf(this.port));
    }
}
