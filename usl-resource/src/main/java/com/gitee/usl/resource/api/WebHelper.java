package com.gitee.usl.resource.api;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.exception.UslExecuteException;
import org.smartboot.http.common.enums.HeaderNameEnum;
import org.smartboot.http.common.enums.HeaderValueEnum;
import org.smartboot.http.common.enums.HttpStatus;
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
public interface WebHelper {
    ThreadLocal<HttpRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();
    ThreadLocal<HttpResponse> RESPONSE_THREAD_LOCAL = new ThreadLocal<>();
    ThreadLocal<USLRunner> RUNNER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 清空所有的线程变量
     */
    static void remove() {
        RUNNER_THREAD_LOCAL.remove();
        REQUEST_THREAD_LOCAL.remove();
        RESPONSE_THREAD_LOCAL.remove();
    }

    /**
     * 重定向
     *
     * @param location 重定向地址
     */
    default void redirect(String location) {
        HttpResponse response = RESPONSE_THREAD_LOCAL.get();
        response.setHttpStatus(HttpStatus.SEE_OTHER);
        response.setHeader(HeaderNameEnum.LOCATION.getName(), location);
    }

    /**
     * 结果转 JSON
     *
     * @param result 结果
     */
    default void writeToJson(Object result) {
        HttpResponse response = RESPONSE_THREAD_LOCAL.get();
        try {
            response.setContentType(HeaderValueEnum.APPLICATION_JSON.getName() + StringConstant.CONTENT_TYPE_SUFFIX);
            response.write(JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UslExecuteException(e);
        }
    }

    /**
     * 结果转 TEXT
     *
     * @param result 结果
     */
    default void writeToText(Object result) {
        HttpResponse response = RESPONSE_THREAD_LOCAL.get();
        try {
            response.setContentType(HeaderValueEnum.DEFAULT_CONTENT_TYPE.getName());
            response.write(String.valueOf(result).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UslExecuteException(e);
        }
    }

    /**
     * 结果转输出流
     *
     * @param stream 输入流
     */
    default void writeToStream(InputStream stream) {
        IoUtil.copy(stream, RESPONSE_THREAD_LOCAL.get().getOutputStream());
    }

    /**
     * 获取指定类型的参数
     *
     * @param name 参数名称
     * @param type 参数类型
     * @param <T>  参数泛型
     * @return 参数值
     */
    default <T> T getParam(String name, Class<T> type) {
        HttpRequest request = REQUEST_THREAD_LOCAL.get();
        return Convert.convert(type, request.getParameter(name));
    }

    /**
     * 请求解析为 Map<K, V>
     *
     * @return 转换结果
     * @throws IOException 转换异常
     */
    default <K, V> Map<K, V> parseToMap() throws IOException {
        return this.parseToObj(new TypeReference<Map<K, V>>() {
        });
    }

    /**
     * 请求解析为 List<T>
     *
     * @param <T> 泛型
     * @return 转换结果
     * @throws IOException 转换异常
     */
    default <T> List<T> parseToObj() throws IOException {
        return this.parseToObj(new TypeReference<List<T>>() {
        });
    }

    /**
     * 请求解析为复杂对象
     *
     * @param typeReference 复杂类型
     * @param <T>           泛型
     * @return 转换结果
     * @throws IOException 转换异常
     */
    default <T> T parseToObj(TypeReference<T> typeReference) throws IOException {
        JSONObject parsed = JSON.parseObject(REQUEST_THREAD_LOCAL.get().getInputStream(), StandardCharsets.UTF_8);
        return parsed.to(typeReference);
    }

    /**
     * 请求解析为简单对象
     *
     * @param type 简单类型
     * @param <T>  泛型
     * @return 转换结果
     * @throws IOException 转换异常
     */
    default <T> T parseToObj(Class<T> type) throws IOException {
        JSONObject parsed = JSON.parseObject(REQUEST_THREAD_LOCAL.get().getInputStream(), StandardCharsets.UTF_8);
        return parsed.to(type);
    }
}
