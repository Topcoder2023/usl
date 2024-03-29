package com.gitee.usl.function.base;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.exception.USLException;
import com.gitee.usl.grammar.utils.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hongda.li
 */
@SuppressWarnings("unused")
@FunctionGroup
public class VisitorFunction {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Function("get_env")
    public Object getEnv(Env env, String name) {
        if (CharSequenceUtil.isBlank(name)) {
            return null;
        }
        return env.get(name);
    }

    @Function("set_env")
    public Object setEnv(Env env, String name, Object value) {
        if (CharSequenceUtil.isBlank(name)) {
            return null;
        }
        env.put(name, value);
        return value;
    }

    @Function("env")
    public Map<String, Object> env(Env env) {
        return new LinkedHashMap<>(env);
    }

    @Function("get")
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
            logger.warn("对象属性获取失败", new USLException(e));
            return null;
        }
    }

    @Function("set")
    public Object set(Object obj, String fieldName, Object value) {
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
            logger.warn("对象属性设置失败", new USLException(e));
            return null;
        }
    }

    @Function("invoke")
    public Object invoke(Object obj, String methodName, Object... params) {
        if (obj == null || CharSequenceUtil.isBlank(methodName)) {
            return null;
        }

        try {
            if (ArrayUtil.isEmpty(params)) {
                return ReflectUtil.invoke(obj, methodName);
            } else {
                return ReflectUtil.invoke(obj, methodName, params);
            }
        } catch (UtilException e) {
            logger.warn("对象方法调用失败", new USLException(e));
            return null;
        }
    }

    @Function("find")
    public Object find(Object obj, String expression) {
        try {
            return new BeanPath(expression).get(obj);
        } catch (Exception e) {
            logger.warn("对象属性查找失败", new USLException(e));
            return null;
        }
    }
}
