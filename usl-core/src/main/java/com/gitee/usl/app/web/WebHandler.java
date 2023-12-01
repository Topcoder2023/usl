package com.gitee.usl.app.web;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.gitee.usl.infra.exception.UslExecuteException;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author hongda.li
 */
public interface WebHandler {
    /**
     * 获取 WEB 路由
     *
     * @return 路由配置
     */
    String getRoute();

    /**
     * 执行业务逻辑
     *
     * @param request  请求
     * @param response 响应
     */
    void doHandle(HttpRequest request, HttpResponse response);

    /**
     * 结果转 JSON
     *
     * @param response 响应
     * @param result   结果
     */
    default void writeToJson(HttpResponse response, Object result) {
        try {
            response.write(JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UslExecuteException(e);
        }
    }

    /**
     * 结果转 TEXT
     *
     * @param response 响应
     * @param result   结果
     */
    default void writeToText(HttpResponse response, Object result) {
        try {
            response.write(String.valueOf(result).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UslExecuteException(e);
        }
    }

    /**
     * 结果转输出流
     *
     * @param stream   输入流
     * @param response 响应
     */
    default void writeToStream(InputStream stream, HttpResponse response) {
        IoUtil.copy(stream, response.getOutputStream());
    }

    /**
     * 请求解析为 Map<K, V>
     *
     * @param request 请求
     * @return 转换结果
     * @throws IOException 转换异常
     */
    default <K, V> Map<K, V> parseToMap(HttpRequest request) throws IOException {
        return this.parseToObj(request, new TypeReference<Map<K, V>>() {
        });
    }

    /**
     * 请求解析为 List<T>
     *
     * @param request 请求
     * @param <T>     泛型
     * @return 转换结果
     * @throws IOException 转换异常
     */
    default <T> List<T> parseToObj(HttpRequest request) throws IOException {
        return this.parseToObj(request, new TypeReference<List<T>>() {
        });
    }

    /**
     * 请求解析为复杂对象
     *
     * @param request       请求
     * @param typeReference 复杂类型
     * @param <T>           泛型
     * @return 转换结果
     * @throws IOException 转换异常
     */
    default <T> T parseToObj(HttpRequest request, TypeReference<T> typeReference) throws IOException {
        JSONObject parsed = JSON.parseObject(request.getInputStream(), StandardCharsets.UTF_8);
        return parsed.to(typeReference);
    }

    /**
     * 请求解析为简单对象
     *
     * @param request 请求
     * @param type    简单类型
     * @param <T>     泛型
     * @return 转换结果
     * @throws IOException 转换异常
     */
    default <T> T parseToObj(HttpRequest request, Class<T> type) throws IOException {
        JSONObject parsed = JSON.parseObject(request.getInputStream(), StandardCharsets.UTF_8);
        return parsed.to(type);
    }

}
