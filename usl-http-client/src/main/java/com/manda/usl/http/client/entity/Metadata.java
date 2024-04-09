package com.manda.usl.http.client.entity;

import com.manda.usl.http.client.HttpClient;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author hongda.li
 */
@Data
public class Metadata {
    /**
     * HTTP 接口
     */
    private final Class<?> client;
    /**
     * HTTP 客户端
     */
    private final HttpClient httpClient;
    /**
     * 方法名-路径名
     */
    private final Map<String, String> pathMapping = new LinkedHashMap<>();

    /**
     * 方法名-请求方法
     */
    private final Map<String, String> methodMapping = new LinkedHashMap<>();

    public Metadata(Class<?> client, HttpClient httpClient) {
        this.client = client;
        this.httpClient = httpClient;
    }
}
