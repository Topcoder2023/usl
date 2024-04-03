package com.manda.usl.http.client;

import cn.zhxu.okhttps.HTTP;
import cn.zhxu.okhttps.OkHttps;
import cn.zhxu.okhttps.fastjson2.Fastjson2MsgConvertor;
import com.manda.usl.http.client.entity.ClientConfiguration;
import com.manda.usl.http.client.mapping.HttpInterceptor;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.common.Configuration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@Slf4j
@Getter
@Singleton(whenNotExist = HttpClient.class)
public class HttpClient {

    private final HTTP httpClient;

    private final List<HttpInterceptor> interceptors;

    public HttpClient(Configuration configuration, List<HttpInterceptor> interceptors) {
        this.interceptors = interceptors;
        ClientConfiguration config = configuration.read("http.client", ClientConfiguration.class);
        log.debug("Http-Client configuration : {}", config);

        httpClient = HTTP.builder()
                .addMsgConvertor(new Fastjson2MsgConvertor())
                .charset(StandardCharsets.UTF_8)
                .bodyType(OkHttps.JSON)
                .config((OkHttpClient.Builder builder) -> {
                    TimeUnit timeUnit = config.getTimeUnit();
                    // 读取超时时间（默认10秒）
                    builder.readTimeout(config.getReadTimeout(), timeUnit);
                    // 写入超时时间（默认10秒）
                    builder.writeTimeout(config.getWriteTimeout(), timeUnit);
                    // 连接超时时间（默认10秒）
                    builder.connectTimeout(config.getConnectTimeout(), timeUnit);
                    // 连接池
                    ConnectionPool pool = new ConnectionPool(config.getMaxIdle(), config.getKeepAlive(), timeUnit);
                    // 最大活跃数与保持时间
                    builder.connectionPool(pool);
                    // 拦截器
                    interceptors.stream()
                            .sorted(Comparator.comparingInt(HttpInterceptor::order))
                            .forEach(builder::addInterceptor);
                })
                .build();
    }

}
