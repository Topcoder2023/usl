package com.manda.usl.manage.gui.api;

import com.manda.usl.kernel.annotation.Mapping;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.manage.gui.entity.SystemInfo;
import com.manda.usl.manage.gui.service.SystemService;

import java.util.List;

/**
 * @author hongda.li
 */
@Mapping("/system")
@Singleton
public class SystemApi {

    private final SystemService systemService;

    public SystemApi(SystemService systemService) {
        this.systemService = systemService;
    }

    @Mapping("/info")
    public List<SystemInfo> info() {
        return systemService.info();
    }
}
