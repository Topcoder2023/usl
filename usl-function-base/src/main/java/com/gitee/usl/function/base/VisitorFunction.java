package com.gitee.usl.function.base;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.infra.exception.UslException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author hongda.li
 */
@Func
public class VisitorFunction {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Func("get")
    public Object get(Object obj, String fieldName) {
        return ReflectUtil.getFieldValue(obj, fieldName);
    }

    @Func("set")
    public Object set(Object obj, String fieldName, Object value) {
        if (obj == null || CharSequenceUtil.isBlank(fieldName)) {
            return null;
        }

        Field field = ReflectUtil.getField(obj.getClass(), fieldName);
        if (field == null) {
            return null;
        }

        try {
            ReflectUtil.setFieldValue(obj, field, value);
        } catch (UtilException e) {
            logger.warn("对象属性设置失败", new UslException(e));
            return null;
        }

        return value;
    }
}
