package com.gitee.usl.grammar;

import com.gitee.usl.api.annotation.Description;

import java.util.function.Consumer;

/**
 * @author hongda.li
 */
@Description("异常处理器")
public interface ExceptionHandler extends Consumer<Throwable> {
}
