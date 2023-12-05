package com.gitee.usl.function.base;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.infra.exception.UslException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author hongda.li
 */
@Func
public class VisitorFunction {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Func("get")
    public Object get(Object obj, String fieldName) {
        if (obj == null || CharSequenceUtil.isBlank(fieldName)) {
            return null;
        }

        try {
            if (obj instanceof Map) {
                return ((Map<?, ?>) obj).get(fieldName);
            }

            if (obj instanceof List) {
                return ((List<?>) obj).get(Integer.parseInt(fieldName));
            }

            if (obj.getClass().isArray()) {
                return Array.get(obj, Integer.parseInt(fieldName));
            }

            return ReflectUtil.getFieldValue(obj, fieldName);
        } catch (Exception e) {
            logger.warn("对象属性获取失败", new UslException(e));
            return null;
        }
    }

    @Func("set")
    public Object set(USLRunner runner, Object obj, String fieldName, Object value) {
        if (obj == null || CharSequenceUtil.isBlank(fieldName)) {
            return null;
        }

        try {
            if (obj instanceof Map) {
                ReflectUtil.invoke(obj, "put", fieldName, value);
                return value;
            }

            if (obj instanceof List) {
                ReflectUtil.invoke(obj, "set", Integer.parseInt(fieldName), value);
                return value;
            }

            if (obj.getClass().isArray()) {
                Array.set(obj, Integer.parseInt(fieldName), value);
                return value;
            }

            Field field = ReflectUtil.getField(obj.getClass(), fieldName);
            if (field == null) {
                return null;
            }

            ReflectUtil.setFieldValue(obj, field, value);
            return value;
        } catch (UtilException e) {
            logger.warn("对象属性设置失败", new UslException(e));
            return null;
        }
    }
}
