package com.manda.usl.http.client;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.TypeUtil;
import cn.zhxu.okhttps.HttpResult;
import com.manda.usl.http.client.entity.Metadata;
import com.manda.usl.http.client.mapping.MetadataParser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author hongda.li
 */
public class ClientExecutor implements InvocationHandler {
    private final Metadata metadata;

    public ClientExecutor(Class<?> client, HttpClient httpClient) {
        this.metadata = new Metadata(client, httpClient);
        MetadataParser.parse(this.metadata);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String name = method.getName();
        HttpClient httpClient = metadata.getHttpClient();
        String path = metadata.getPathMapping().get(name);
        String methodName = metadata.getMethodMapping().get(name);
        Assert.notNull(path, "Http path can't be null");
        Assert.notNull(methodName, "Http method can't be null");

        Class<?> returnType = method.getReturnType();
        HttpResult request = httpClient.getHttpClient().sync(path).request(methodName);

        if (List.class.equals(returnType)) {
            Type argument = TypeUtil.getTypeArgument(method.getGenericReturnType());
            Class<?> elementType;
            if (argument instanceof ParameterizedType) {
                elementType = (Class<?>) ((ParameterizedType) argument).getRawType();
            } else {
                elementType = Object.class;
            }
            return request.getBody().toList(elementType);
        } else if (String.class.equals(returnType)) {
            return request.getBody().toString();
        } else {
            return request.getBody().toBean(returnType);
        }
    }
}
