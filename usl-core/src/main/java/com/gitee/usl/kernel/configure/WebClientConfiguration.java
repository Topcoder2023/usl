package com.gitee.usl.kernel.configure;

import com.gitee.usl.app.web.WebClientManager;

/**
 * @author hongda.li
 */
public class WebClientConfiguration {
    private WebClientManager clientManager;

    public WebClientManager clientManager() {
        return clientManager;
    }

    public WebClientConfiguration setClientManager(WebClientManager clientManager) {
        this.clientManager = clientManager;
        return this;
    }
}
