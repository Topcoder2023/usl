package com.gitee.usl.kernel.configure;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class WebServerConfiguration {
    private final Configuration configuration;
    private String name;
    private int port;
    private boolean debug;
    private String path;

    public WebServerConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration finish() {
        return this.configuration;
    }

    public Configuration getConfiguration() {
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

    public boolean isDebug() {
        return debug;
    }

    public WebServerConfiguration setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public String getPath() {
        return path;
    }

    public WebServerConfiguration setPath(String path) {
        this.path = path;
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
