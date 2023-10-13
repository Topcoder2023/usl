package com.gitee.usl.kernel.configure;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class WebServerConfiguration {
    private final UslConfiguration configuration;
    private String name;
    private int port;

    public WebServerConfiguration(UslConfiguration configuration) {
        this.configuration = configuration;
    }

    public UslConfiguration getConfiguration() {
        return configuration;
    }

    public String getName() {
        return name;
    }

    public WebServerConfiguration setName(String name) {
        this.name = name;
        return this;
    }

    public int getPort() {
        return port;
    }

    public WebServerConfiguration setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WebServerConfiguration.class.getSimpleName() + "[", "]")
                .add("configuration=" + configuration)
                .add("name='" + name + "'")
                .add("port=" + port)
                .toString();
    }
}
