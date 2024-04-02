package com.manda.usl.kernel.bean;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.manda.usl.kernel.annotation.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;
import java.util.Optional;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BeanId {

    private String name;

    private Class<?> type;

    public static BeanId of(Object bean) {
        Objects.requireNonNull(bean);
        return new BeanId()
                .setType(bean.getClass())
                .defaultName()
                .check();
    }

    public static BeanId of(String name, Object bean) {
        Objects.requireNonNull(bean);
        return new BeanId()
                .setType(bean.getClass())
                .setName(name)
                .check();
    }

    public BeanId setName(Class<?> toInstantiated) {
        String name = AnnotationUtil.getAnnotationValue(toInstantiated, Singleton.class);
        if (CharSequenceUtil.isBlank(name)) {
            this.defaultName();
        } else {
            this.name = name;
        }
        return this;
    }

    public BeanId setName(String name) {
        this.name = name;
        return this;
    }

    public BeanId defaultName() {
        Objects.requireNonNull(type);
        this.name = Optional.ofNullable(this.name).orElse(CharSequenceUtil.lowerFirst(type.getSimpleName()));
        return this;
    }

    public BeanId check() {
        Objects.requireNonNull(type);
        Objects.requireNonNull(name);
        return this;
    }
}
