package com.manda.usl.kernel.bean;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.manda.usl.kernel.annotation.PostHandle;
import com.manda.usl.kernel.bean.event.EventPublisher;
import com.manda.usl.kernel.bean.event.PreparedEvent;
import com.manda.usl.kernel.bean.event.ReadyEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
@Slf4j
public final class BeanContext {
    private static final Map<BeanId, Object> CONTAINER = new HashMap<>(1 << 10);

    public static Set<Class<?>> readonly() {
        return CONTAINER.keySet()
                .stream()
                .map(BeanId::getType)
                .collect(Collectors.toSet());
    }

    public static void refresh() {
        log.debug("Prepared to refresh bean context");
        CONTAINER.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == null)
                .map(Map.Entry::getKey)
                .map(BeanCreator::new)
                .toList()
                .forEach(BeanCreator::create);

        CONTAINER.values().removeIf(Objects::isNull);

        EventPublisher.prepared();
        EventPublisher.publish(new PreparedEvent());

        log.debug("Prepared to solve dependency");
        CONTAINER.values()
                .stream()
                .map(BeanSolver::new)
                .forEach(BeanSolver::solve);

        EventPublisher.publish(new ReadyEvent());
    }

    public static void register(Class<?> toInstantiated) {
        Objects.requireNonNull(toInstantiated);
        register(new BeanId()
                .setType(toInstantiated)
                .setName(toInstantiated)
                .check(), null);
    }

    public static void register(Object bean) {
        register(BeanId.of(bean), bean);
    }

    public static void register(String name, Object bean) {
        register(BeanId.of(name, bean), bean);
    }

    public static void register(BeanId beanId, Object bean) {
        Object exists = CONTAINER.get(beanId);
        Assert.isTrue(exists == null, "Bean mapping already exists : {}", beanId);
        CONTAINER.put(beanId, bean);
        if (bean != null) {
            log.debug("Register bean mapping : {}", beanId);
            callback(bean);
        }
    }

    public static Object findOne(BeanId beanId) {
        return CONTAINER.get(beanId);
    }

    public static <B> B findFirst(Class<B> type) {
        return CollUtil.getFirst(find(type));
    }

    public static <B> B findFirst(Class<B> type, String name) {
        return CollUtil.getFirst(find(type, name));
    }

    public static <B> List<B> find(Class<B> type, String name) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);
        return CONTAINER.entrySet()
                .stream()
                .filter(entry -> type.isAssignableFrom(entry.getKey().getType()))
                .filter(entry -> Objects.equals(entry.getKey().getName(), name))
                .map(Map.Entry::getValue)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    public static <B> List<B> find(Class<B> type) {
        Objects.requireNonNull(type);
        return CONTAINER.entrySet()
                .stream()
                .filter(entry -> type.isAssignableFrom(entry.getKey().getType()))
                .map(Map.Entry::getValue)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    public static List<Object> with(Class<? extends Annotation> type) {
        return find(beanId -> AnnotationUtil.hasAnnotation(beanId.getType(), type));
    }

    public static List<Object> find(Predicate<BeanId> predicate) {
        Objects.requireNonNull(predicate);
        return CONTAINER.entrySet()
                .stream()
                .filter(entry -> predicate.test(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private static void callback(Object bean) {
        if (bean instanceof RegisterCallback callback) {
            callback.run();
        }
        Method[] methods = ReflectUtil.getMethods(bean.getClass(), method -> AnnotationUtil.hasAnnotation(method, PostHandle.class));
        if (ArrayUtil.isEmpty(methods)) {
            return;
        }
        for (Method method : methods) {
            int count = method.getParameterCount();
            Assert.isTrue(count == 0, "Post-handle-method doesn't allow parameters to exist");
            ReflectUtil.invoke(bean, method);
        }
    }
}
