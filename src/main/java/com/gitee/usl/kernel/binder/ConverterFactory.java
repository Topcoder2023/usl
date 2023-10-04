package com.gitee.usl.kernel.binder;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.TypeUtil;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.utils.ServiceSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author hongda.li
 */
public class ConverterFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterFactory.class);
    private static final ConverterFactory INSTANCE = new ConverterFactory();
    private static final Map<Class<?>, UslConverter<?>> CONVERTER_MAP;

    static {
        CONVERTER_MAP = HashMap.newHashMap(NumberConstant.COMMON_SIZE);
        ServiceSearcher.searchAll(UslConverter.class).forEach(converter -> {
            Type type = TypeUtil.getTypeArgument(converter.getClass());
            CONVERTER_MAP.put((Class<?>) type, converter);
            LOGGER.info("Register USL converter - [{}]", converter.getClass().getName());
        });
    }

    private ConverterFactory() {
    }

    public static ConverterFactory getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <C> UslConverter<C> getConverter(Class<C> type) {
        return Optional.ofNullable((UslConverter<C>) CONVERTER_MAP.get(type))
                .orElse(target -> Convert.convert(type, target));
    }
}
