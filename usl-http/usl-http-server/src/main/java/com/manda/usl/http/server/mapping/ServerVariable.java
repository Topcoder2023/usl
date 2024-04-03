package com.manda.usl.http.server.mapping;

import cn.hutool.core.convert.Convert;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hongda.li
 */
public class ServerVariable {
    public static final String HTTP_REQUEST = "HttpRequest";
    public static final String HTTP_RESPONSE = "HttpResponse";
    private static final ThreadLocal<Map<String, Object>> TEMP_VALUE_THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    public static void init(HttpRequest request, HttpResponse response) {
        set(HTTP_REQUEST, request);
        set(HTTP_RESPONSE, response);
    }

    public static HttpRequest request() {
        return get(HTTP_REQUEST, HttpRequest.class);
    }

    public static HttpResponse response() {
        return get(HTTP_RESPONSE, HttpResponse.class);
    }

    public static void set(String key, Object value) {
        TEMP_VALUE_THREAD_LOCAL.get().put(key, value);
    }

    public static <T> T get(String key, Class<T> type) {
        return Convert.convert(type, TEMP_VALUE_THREAD_LOCAL.get().get(key));
    }

    public static void remove() {
        TEMP_VALUE_THREAD_LOCAL.remove();
    }
}
