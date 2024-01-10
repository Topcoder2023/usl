package com.gitee.usl.logger;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.ansi.AnsiColor;
import cn.hutool.core.lang.ansi.AnsiEncoder;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import org.slf4j.Marker;
import org.slf4j.event.Level;

import java.io.Serial;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.System.out;

/**
 * 格式化日志模板并根据日志级别向控制台输出彩色日志信息
 *
 * @author hongda.li
 */
public class UslLogger extends AbstractUslEnabledLogger {
    @Serial
    private static final long serialVersionUID = -6857629823140297158L;

    public UslLogger(String name) {
        this.name = name;
    }

    /**
     * 控制台打印时间的颜色
     */
    private static final AnsiColor COLOR_TIME = AnsiColor.WHITE;

    /**
     * 控制台打印类名（日志名称）的颜色
     */
    private static final AnsiColor COLOR_CLASSNAME = AnsiColor.MAGENTA;

    /**
     * 日志级别与日志对应的颜色映射关系
     */
    private static final Function<Level, AnsiColor> COLOR_MAPPING = level -> switch (level) {
        case INFO -> AnsiColor.BRIGHT_CYAN;
        case ERROR -> AnsiColor.BRIGHT_RED;
        case TRACE -> AnsiColor.BRIGHT_BLUE;
        case WARN -> AnsiColor.BRIGHT_YELLOW;
        default -> AnsiColor.WHITE;
    };

    @Override
    protected String getFullyQualifiedCallerName() {
        return this.name;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level,
                                               Marker marker,
                                               String format,
                                               Object[] arguments,
                                               Throwable throwable) {
        // 根据日志级别获取对应的颜色定义
        AnsiColor color = COLOR_MAPPING.apply(level);

        // 根据颜色定义生成特殊编码的模板
        final String template = AnsiEncoder.encode(COLOR_TIME, "[%s]", color, " [%-5s]%s", COLOR_CLASSNAME, "%-45s: ", color, "%s%n");

        // 生成 yyyy-MM-dd HH:mm:ss.SSS 格式的时间
        final String time = DatePattern.NORM_DATETIME_MS_FORMAT.format(new Date());

        // 格式化消息模板，将其中的 {} 替换为对应的参数值
        final String message = CharSequenceUtil.format(format, arguments);

        // 格式化特殊编码模板并输出到控制台
        out.format(template, time, level.name(), " - ", ClassUtil.getShortClassName(this.getName()), message);

        // 若异常存在则打印到错误流中
        Optional.ofNullable(throwable).ifPresent(Throwable::printStackTrace);
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }
}
