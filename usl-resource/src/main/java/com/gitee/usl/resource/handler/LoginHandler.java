package com.gitee.usl.resource.handler;

import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.resource.Returns;
import com.gitee.usl.resource.api.WebHandler;
import com.gitee.usl.resource.filter.SecurityFilter;
import com.google.auto.service.AutoService;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
@AutoService(WebHandler.class)
public class LoginHandler implements WebHandler {
    private static final String PATH = "/api/login";

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        String username = this.getParam("username", String.class);
        String password = this.getParam("password", String.class);

        if ("admin".equals(username) && "123456".equals(password)) {
            this.writeToJson(Returns.success(SecurityFilter.getTokenValue()));
        } else {
            this.writeToJson(Returns.failure());
        }
    }
}
