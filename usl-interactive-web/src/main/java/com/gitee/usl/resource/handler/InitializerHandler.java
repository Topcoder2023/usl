package com.gitee.usl.resource.handler;


import com.gitee.usl.resource.entity.Returns;
import com.gitee.usl.resource.entity.TreeInfo;
import com.gitee.usl.resource.api.WebHandler;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.List;

/**
 * @author hongda.li
 */
public class InitializerHandler implements WebHandler {
    private static final String PATH = "/usl/admin/api/initializer";
    private List<TreeInfo> infoCache;

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        this.writeToJson(Returns.success());
    }
}
