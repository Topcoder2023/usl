package com.gitee.usl.api.service;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import com.gitee.usl.api.EnvLoader;
import com.gitee.usl.api.annotation.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author hongda.li
 */
@Order(PropertiesEnvLoader.PROPERTIES_ENV_LOADER_ORDER)
public class PropertiesEnvLoader implements EnvLoader {
    /**
     * 配置文件环境变量加载器的顺序
     */
    public static final int PROPERTIES_ENV_LOADER_ORDER = Integer.MIN_VALUE + 200;

    /**
     * 默认的配置文件的名称
     */
    private static final String CONFIG_FILE_NAME = "config.properties";

    private final Logger logger = LoggerFactory.getLogger(PropertiesEnvLoader.class);
    private Properties properties;

    @Override
    public String fetch(String name) {
        return this.loadProperty().getProperty(name);
    }

    /**
     * 尝试从类路径下加载默认的配置文件
     * 即使配置文件不存在也不会抛出异常
     *
     * @return 加载后的配置文件
     */
    private synchronized Properties loadProperty() {
        // 如果配置文件已被初始化则直接返回
        // 即使初始化失败也会返回
        if (this.properties != null) {
            return this.properties;
        }

        // 初始化配置文件
        properties = new Properties();

        // 加载配置文件
        try {
            final Resource resource = ResourceUtil.getResourceObj(CONFIG_FILE_NAME);
            try (final BufferedReader reader = resource.getReader(StandardCharsets.UTF_8)) {
                properties.load(reader);
            }
        } catch (Exception e) {
            logger.warn("No such configuration file exists in classpath. {}", CONFIG_FILE_NAME);
        }

        return properties;
    }
}
