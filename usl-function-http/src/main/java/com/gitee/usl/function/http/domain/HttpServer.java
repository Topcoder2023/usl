package com.gitee.usl.function.http.domain;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.server.SimpleServer;
import lombok.Data;

/**
 * @author hongda.li
 */
@Data
public class HttpServer {
    /**
     * 本机地址
     */
    private final String host;
    /**
     * 端口号
     */
    private final int port;
    /**
     * 服务器代理
     */
    private final SimpleServer proxy;

    public HttpServer(int port) {
        this.host = NetUtil.getLocalhostStr();
        this.port = port;
        this.proxy = new SimpleServer(host, port);
        this.proxy.setExecutor(runnable -> Thread.ofVirtual().start(runnable));
    }

}
