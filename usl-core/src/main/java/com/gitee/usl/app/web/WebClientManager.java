package com.gitee.usl.app.web;

import cn.zhxu.okhttps.HTTP;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.configure.WebClientConfiguration;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@AutoService(Initializer.class)
public class WebClientManager implements Initializer {
    private HTTP http;

    @Override
    public void doInit(Configuration configuration) {
        WebClientConfiguration config = configuration.configWebClient();
        config.setClientManager(this);
    }

    public HTTP http() {
        return http;
    }
}
