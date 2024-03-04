package com.gitee.usl.resource;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.WebInteractive;
import com.gitee.usl.domain.ResourceParam;

/**
 * B-S架构
 * 接收来自外部的请求
 * 采用内嵌式 WEB-SERVER 实现
 * 考虑到轻量级依赖，此处未选择 Tomcat、Undertow、Jetty 等，而是选择了 Smart-http-server
 *
 * @author hongda.li
 */
public class WebInteractiveImpl implements WebInteractive {

    @Override
    public void open(USLRunner runner) {
        runner.run(new ResourceParam("route/main.js"));
    }
}
