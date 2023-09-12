package com.gitee.usl.infra.logger;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * USL 日志服务提供者
 * 对接 SLF4J 日志门面
 * SPI文件将在后续正式版本中被移除
 * classpath://META-INF/services/org.slf4j.spi.SLF4JServiceProvider
 *
 * @author hongda.li
 */
public class UslServiceProvider implements SLF4JServiceProvider {
    /**
     * USL 日志服务提供者所需的 SLF4J 的最低版本
     * 默认最低版本为 2.0.*
     */
    private static final String REQUESTED_API_VERSION = "2.0.9";

    /**
     * Mapped Diagnostic Context 映射调试上下文
     * 日志系统提供的一种方便在多线程条件下记录日志的功能
     */
    private MDCAdapter mdcAdapter;

    /**
     * 日志工厂
     * 创建自定义日志记录器
     */
    private ILoggerFactory loggerFactory;

    /**
     * Marker 创建工厂
     */
    private IMarkerFactory markerFactory;

    @Override
    public ILoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return this.markerFactory;
    }

    @Override
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public MDCAdapter getMDCAdapter() {
        return this.mdcAdapter;
    }

    @Override
    public String getRequestedApiVersion() {
        return REQUESTED_API_VERSION;
    }

    @Override
    public void initialize() {
        // 默认使用空的映射调试上下文适配器
        // 该适配器不会采取任何操作
        this.mdcAdapter = new NOPMDCAdapter();

        // 默认使用 USL 自定义的日志工厂
        // 该日志工厂会创建 USL 自定义日志记录器
        this.loggerFactory = new UslLoggerFactory();

        // 默认使用 SLF4J 提供的基础 Marker 工厂
        this.markerFactory = new BasicMarkerFactory();
    }
}
