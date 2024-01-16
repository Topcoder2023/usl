package com.gitee.usl.resource.api;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.gitee.usl.Runner;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.exception.USLException;
import com.gitee.usl.infra.exception.USLExecuteException;
import org.smartboot.http.common.enums.HeaderNameEnum;
import org.smartboot.http.common.enums.HeaderValueEnum;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author hongda.li
 */
public interface WebHelper {
    ThreadLocal<HttpRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();
    ThreadLocal<HttpResponse> RESPONSE_THREAD_LOCAL = new ThreadLocal<>();
    ThreadLocal<Runner> RUNNER_THREAD_LOCAL = new ThreadLocal<>();

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
            response.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new USLExecuteException(e);
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
            throw new USLExecuteException(e);
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
     * 请求解析为简单对象
     *
     * @param type 简单类型
     * @param <T>  泛型
     * @return 转换结果
     */
    default <T> List<T> parseToArray(Class<T> type) {
        try {
            return JSONUtil.toList(IoUtil.read(REQUEST_THREAD_LOCAL.get().getInputStream(), StandardCharsets.UTF_8), type);
        } catch (IOException e) {
            throw new USLException(e);
        }
    }

    /**
     * 请求解析为简单对象
     *
     * @param type 简单类型
     * @param <T>  泛型
     * @return 转换结果
     */
    default <T> T parseToObj(Class<T> type) {
        try {
            return JSONUtil.toBean(IoUtil.read(REQUEST_THREAD_LOCAL.get().getInputStream(), StandardCharsets.UTF_8), type);
        } catch (IOException e) {
            throw new USLException(e);
        }
    }
}
