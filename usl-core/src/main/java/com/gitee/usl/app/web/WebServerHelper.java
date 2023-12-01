package com.gitee.usl.app.web;

import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
public class WebServerHelper {
    private WebServerHelper() {
    }

    private static final ThreadLocal<HttpRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<HttpResponse> RESPONSE_THREAD_LOCAL = new ThreadLocal<>();

    public static void setRequest(HttpRequest request) {
        REQUEST_THREAD_LOCAL.set(request);
    }

    public static HttpRequest getRequest() {
        return REQUEST_THREAD_LOCAL.get();
    }

    public static void setResponse(HttpResponse response) {
        RESPONSE_THREAD_LOCAL.set(response);
    }

    public static HttpResponse getResponse() {
        return RESPONSE_THREAD_LOCAL.get();
    }

    public static void remove() {
        REQUEST_THREAD_LOCAL.remove();
        RESPONSE_THREAD_LOCAL.remove();
    }
}
