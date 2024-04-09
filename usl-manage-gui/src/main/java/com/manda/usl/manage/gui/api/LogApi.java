package com.manda.usl.manage.gui.api;

import com.manda.usl.kernel.annotation.Mapping;
import com.manda.usl.kernel.annotation.Param;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.manage.gui.entity.LogView;
import com.manda.usl.manage.gui.service.LogService;

import java.util.List;

/**
 * @author hongda.li
 */
@Singleton
@Mapping("/log")
public class LogApi {

    private final LogService logService;

    public LogApi(LogService logService) {
        this.logService = logService;
    }

    @Mapping("/list")
    public List<LogView> list(@Param("type") String type,
                              @Param("time") String time,
                              @Param("level") String level) {
        return logService.list(type, time, level);
    }
}
