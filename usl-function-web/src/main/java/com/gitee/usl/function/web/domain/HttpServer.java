package com.gitee.usl.function.web.domain;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.server.SimpleServer;
import com.gitee.usl.Runner;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class HttpServer {
    private final Runner runner;
    private final String host;
    private final int port;
    private final SimpleServer server;

    public HttpServer(Runner runner, int port) {
        this.runner = runner;
        this.host = NetUtil.getLocalhostStr();
        this.port = port;
        this.server = new SimpleServer(host, port);
        this.server.setExecutor(runnable -> Thread.ofVirtual().start(runnable));
    }

    public SimpleServer getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public Runner getRunner() {
        return runner;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HttpServer.class.getSimpleName() + "[", "]")
                .add("host=" + host)
                .add("port=" + port)
                .toString();
    }
}
