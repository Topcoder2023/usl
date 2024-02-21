package com.gitee.usl.api;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.exception.USLExecuteException;

import java.util.function.Consumer;

/**
 * @author hongda.li
 */
@Description("异常处理器")
public interface ExceptionHandler extends Consumer<Throwable> {

    @Description("默认的异常处理器")
    ExceptionHandler DEFAULT = error -> {
        throw new USLExecuteException(error);
    };

}
