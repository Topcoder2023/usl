package com.gitee.usl.resource;

import com.gitee.usl.app.web.WebHandler;
import com.google.auto.service.AutoService;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
@AutoService(WebHandler.class)
public class FunctionInfoController implements WebHandler {
    private static final String PATH = "/function";

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        redirect("/static/admin/index");
    }
}
