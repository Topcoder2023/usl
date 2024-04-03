package com.manda.usl.http.server;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.manda.usl.http.server.entity.ServerConfiguration;
import com.manda.usl.http.server.mapping.ServerHandler;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.common.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.http.server.HttpBootstrap;

import java.util.Optional;

/**
 * @author hongda.li
 */
@Slf4j
@Singleton(whenNotExist = Server.class)
public final class Server {
    private final String path;
    private final HttpBootstrap bootstrap;

    public Server(Configuration configuration) {
        ServerConfiguration config = configuration.read("http.server", ServerConfiguration.class);
        log.debug("Http-Server configuration : {}", config);

        this.bootstrap = new HttpBootstrap();
        String host = config.getHost();
        Integer port = config.getPort();
        this.path = URLUtil.normalize(host + StrUtil.COLON + port);
        this.bootstrap.setPort(port);
        this.bootstrap.configuration()
                .host(host)
                .debug(false)
                .bannerEnabled(false)
                .threadNum(config.getThreadNum())
                .writeBufferSize(config.getBufferSize())
                .setHttpIdleTimeout(config.getIdleTimeout())
                .serverName(Optional.ofNullable(config.getName()).orElse(URLUtil.normalize(host + StrUtil.COLON + port)))
                .setMaxRequestSize(config.getMaxRequestSize());
    }

    public void start() {
        log.info("Http-Server listen on : {}", this.path);
        this.bootstrap.httpHandler(new ServerHandler());
        this.bootstrap.start();
    }

    public void shutdown() {
        this.bootstrap.shutdown();
    }
}
