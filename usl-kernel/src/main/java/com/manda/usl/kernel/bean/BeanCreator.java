package com.manda.usl.kernel.bean;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.annotation.Param;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
@Slf4j
public class BeanCreator {
    private final BeanId beanId;

    public BeanCreator(BeanId beanId) {
        this.beanId = beanId;
    }

    public void create() {
        this.create(beanId.getType(), new LinkedHashSet<>());
    }

    private Object create(Class<?> toInstantiated, Set<Class<?>> remember) {
        Object exists = BeanContext.findOne(new BeanId().setType(toInstantiated).setName(toInstantiated));
        if (exists != null) {
            return exists;
        }

        log.debug("Prepared to instantiate bean : {}", toInstantiated.getName());

        if (remember.contains(toInstantiated)) {
            throw new RuntimeException("Cyclic dependency found : " + ObjectUtil.toString(remember));
        } else {
            remember.add(toInstantiated);
        }

        Class<?> whenNotExist = AnnotationUtil.getAnnotation(toInstantiated, Singleton.class).whenNotExist();
        if (!Class.class.equals(whenNotExist)) {
            long count = BeanContext.readonly()
                    .stream()
                    .filter(whenNotExist::isAssignableFrom)
                    .filter(clz -> !whenNotExist.equals(AnnotationUtil.getAnnotation(clz, Singleton.class).whenNotExist()))
                    .map(clz -> this.create(clz, new LinkedHashSet<>()))
                    .filter(Objects::nonNull)
                    .count();
            if (count != 0 || BeanContext.find(whenNotExist).stream().anyMatch(Objects::nonNull)) {
                return null;
            }
        }

        Constructor<?>[] constructors = ReflectUtil.getConstructors(toInstantiated);
        Assert.isTrue(constructors.length == 1, "Too many constructor in class : {}", toInstantiated.getName());

        Constructor<?> constructor = constructors[0];

        Object bean;
        if (constructor.getParameterCount() == 0) {
            log.debug("Use none parameter constructor to instantiate");
            bean = ReflectUtil.newInstance(toInstantiated);
        } else {
            bean = ReflectUtil.newInstance(toInstantiated, this.fetchDependencies(constructor.getParameters(), remember).toArray());
        }

        BeanContext.register(new BeanId()
                .setType(toInstantiated)
                .setName(toInstantiated), bean);

        return bean;
    }

    private List<Object> fetchDependencies(Parameter[] parameters, Set<Class<?>> remember) {
        List<Object> valueList = new ArrayList<>(parameters.length);

        for (Parameter parameter : parameters) {
            Class<?> type = parameter.getType();
            boolean aggregation = List.class.equals(type);
            if (aggregation) {
                Class<?> elementType = (Class<?>) TypeUtil.getTypeArgument(parameter.getParameterizedType());
                log.debug("Try to fetch all of the dependency : {}", elementType.getName());
                valueList.add(BeanContext.readonly().stream()
                        .filter(elementType::isAssignableFrom)
                        .map(clz -> this.create(clz, remember))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            } else {
                log.debug("Try to fetch one of the dependency : {}", type);
                Assert.isTrue(BeanContext.readonly().contains(type), "Not found of bean : {}", type.getName());
                String name = AnnotationUtil.getAnnotationValue(parameter, Param.class);
                Object find = CharSequenceUtil.isBlank(name)
                        ? BeanContext.findFirst(type)
                        : BeanContext.findFirst(type, name);
                if (find == null) {
                    valueList.add(this.create(type, remember));
                } else {
                    valueList.add(find);
                }
            }
        }

        return valueList;
    }
}
