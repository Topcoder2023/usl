package com.manda.usl.kernel.bean;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.manda.usl.kernel.USLRunner;
import com.manda.usl.kernel.annotation.Property;
import com.manda.usl.kernel.common.Configuration;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author hongda.li
 */
@Slf4j
public class BeanSolver {
    private final Object bean;

    public BeanSolver(Object bean) {
        this.bean = bean;
    }

    public void solve() {
        Field[] fields = ReflectUtil.getFields(bean.getClass(), this::filter);
        if (ArrayUtil.isEmpty(fields)) {
            return;
        }

        Configuration config = USLRunner.getConfiguration();

        for (Field field : fields) {
            String name = AnnotationUtil.getAnnotationValue(field, Property.class);
            Object value;
            if (CharSequenceUtil.isBlank(name)) {
                // 根据类型自动注入
                value = BeanContext.findFirst(field.getType());
            } else if (CharSequenceUtil.isWrap(name, "{", "}")) {
                // 根据JSON表达式自动注入
                value = config.read(CharSequenceUtil.unWrap(name, "{", "}"), field.getType());
            } else {
                // 根据类型+名称自动注入
                value = BeanContext.findFirst(field.getType(), name);
            }
            log.debug("Try to bind field value : [{} - {}]", bean.getClass().getName(), field.getName());
            ReflectUtil.setFieldValue(bean, field, value);
        }
    }

    private boolean filter(Field field) {
        return AnnotationUtil.hasAnnotation(field, Property.class) && ReflectUtil.getFieldValue(bean, field) == null;
    }
}
