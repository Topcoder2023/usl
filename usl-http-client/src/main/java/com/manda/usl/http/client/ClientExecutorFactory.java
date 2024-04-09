package com.manda.usl.http.client;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import com.manda.usl.http.client.annotation.Client;
import com.manda.usl.http.client.entity.ClientConfiguration;
import com.manda.usl.kernel.USLRunner;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.bean.BeanId;

import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongda.li
 */
public class ClientExecutorFactory {
    private final ClassLoader classLoader = ClassUtil.getClassLoader();
    private final Map<Class<?>, Object> clientMap = new ConcurrentHashMap<>();

    public ClientExecutorFactory() {
        this.initClient();
    }

    private void initClient() {
        ClientConfiguration config = USLRunner.getConfiguration().read("http.client", ClientConfiguration.class);
        Optional.ofNullable(config.getClients())
                .orElse(Collections.singletonList(ClassUtil.getPackage(USLRunner.getMain())))
                .stream()
                .map(ClassUtil::scanPackage)
                .flatMap(Collection::stream)
                .distinct()
                .filter(clz -> AnnotationUtil.hasAnnotation(clz, Client.class))
                .filter(ClassUtil::isInterface)
                .forEach(client -> {
                    Object clientInstance = getClient(client);
                    BeanContext.register(new BeanId()
                            .setType(client)
                            .defaultName(), clientInstance);
                });
    }

    public Object getClient(Class<?> client) {
        Object proxy = clientMap.get(client);
        if (proxy == null) {
            ClientExecutor executor = new ClientExecutor(client, this.matchHttpClient(client));
            clientMap.put(client, Proxy.newProxyInstance(this.classLoader, new Class[]{client}, executor));
            return this.getClient(client);
        }
        return client.cast(proxy);
    }

    private HttpClient matchHttpClient(Class<?> client) {
        String clientName = AnnotationUtil.getAnnotationValue(client, Client.class);
        if (CharSequenceUtil.isEmpty(clientName)) {
            return BeanContext.findFirst(HttpClient.class);
        } else {
            return BeanContext.findFirst(HttpClient.class, clientName);
        }
    }
}
