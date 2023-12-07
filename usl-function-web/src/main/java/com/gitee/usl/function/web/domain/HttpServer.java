package com.gitee.usl.function.web.domain;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.server.SimpleServer;
import com.gitee.usl.USLRunner;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class HttpServer {
    private final USLRunner runner;
    private final String host;
    private final int port;
    private final SimpleServer server;

    public HttpServer(USLRunner runner, int port) {
        this.runner = runner;
        this.host = NetUtil.getLocalhostStr();
        this.port = port;
        this.server = new SimpleServer(host, port);
        this.server.setExecutor(runner.configuration()
                .configExecutor()
                .executorManager()
                .executor());
    }

    public SimpleServer getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public USLRunner getRunner() {
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
