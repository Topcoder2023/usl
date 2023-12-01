package com.gitee.usl.app.web;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author hongda.li
 */
public abstract class AbstractWebHandler extends HttpServerHandler {
    private static final TypeReference<Map<String, Object>> REFERENCE = new TypeReference<Map<String, Object>>() {
    };

    /**
     * 获取 WEB 路由
     *
     * @return 路由配置
     */
    public abstract String getRoute();

    public void writeToJson(HttpResponse response, Object result) throws IOException {
        response.write(JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8));
    }

    public void writeToStream(InputStream stream, HttpResponse response) {
        IoUtil.copy(stream, response.getOutputStream());
    }

    public Map<String, Object> parseToMap(HttpRequest request) throws IOException {
        return this.parseToObj(request, REFERENCE);
    }

    public <T> T parseToObj(HttpRequest request, TypeReference<T> typeReference) throws IOException {
        JSONObject parsed = JSON.parseObject(request.getInputStream(), StandardCharsets.UTF_8);
        return parsed.to(typeReference);
    }
}
