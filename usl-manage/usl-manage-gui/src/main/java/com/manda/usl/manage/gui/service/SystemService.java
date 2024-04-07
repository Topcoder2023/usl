package com.manda.usl.manage.gui.service;

import cn.hutool.system.*;
import com.manda.usl.http.server.entity.ServerConfiguration;
import com.manda.usl.kernel.USLRunner;
import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.manage.gui.entity.SystemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongda.li
 */
@Singleton
public class SystemService {

    public List<SystemInfo> info() {
        List<SystemInfo> infoList = new ArrayList<>();
        infoList.add(new SystemInfo("USL版本", USLRunner.VERSION));

        String runtime = (System.currentTimeMillis() - USLRunner.getTimestamp()) / 1000 + " 秒";
        infoList.add(new SystemInfo("运行时间", runtime));

        UserInfo userInfo = SystemUtil.getUserInfo();
        infoList.add(new SystemInfo("用户目录", userInfo.getHomeDir()));
        infoList.add(new SystemInfo("临时目录", userInfo.getTempDir()));
        infoList.add(new SystemInfo("用户名称", userInfo.getName()));

        OsInfo osInfo = SystemUtil.getOsInfo();
        infoList.add(new SystemInfo("系统名称", osInfo.getName()));
        infoList.add(new SystemInfo("系统版本", osInfo.getVersion()));

        int processors = SystemUtil.getRuntimeInfo().getRuntime().availableProcessors();
        infoList.add(new SystemInfo("总核心数", processors));

        infoList.add(new SystemInfo("内存大小", SystemUtil.getTotalMemory() / 1024 / 1024 + "MB"));
        infoList.add(new SystemInfo("可用内存", SystemUtil.getFreeMemory() / 1024 / 1024 + "MB"));

        ServerConfiguration config = USLRunner.getConfiguration().read("http.server", ServerConfiguration.class);
        infoList.add(new SystemInfo("主机地址", config.getHost()));
        infoList.add(new SystemInfo("端口地址", config.getPort()));

        return infoList;
    }
}
