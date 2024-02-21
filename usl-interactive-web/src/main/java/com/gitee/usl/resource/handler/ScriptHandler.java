package com.gitee.usl.resource.handler;

import com.gitee.usl.resource.ScriptSearcher;
import com.gitee.usl.resource.api.WebHandler;
import com.gitee.usl.resource.entity.Returns;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
public class ScriptHandler implements WebHandler {
    private static final String PATH = "/usl/admin/api/script";
    private static final String EDIT = "edit";
    private static final String BELONGS = "belongs";
    private static final String SCRIPT_NAME = "scriptName";

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        Boolean edit = getParam(EDIT, Boolean.class);
        String belongs = getParam(BELONGS, String.class);
        String scriptName = getParam(SCRIPT_NAME, String.class);

        if (Boolean.TRUE.equals(edit)) {
            this.writeToJson(Returns.success(ScriptSearcher.findOne(scriptName, belongs)));
        } else {
            this.writeToJson(Returns.success(ScriptSearcher.findAllToInfo(scriptName, belongs)));
        }
    }
}
