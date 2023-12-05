package com.gitee.usl.resource.handler;

import cn.hutool.core.io.FileUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.resource.ScriptSearcher;
import com.gitee.usl.resource.api.WebHandler;
import com.gitee.usl.resource.api.WebHelper;
import com.gitee.usl.resource.entity.Returns;
import com.gitee.usl.resource.entity.ScriptInfo;
import com.google.auto.service.AutoService;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.io.File;
import java.util.List;

/**
 * @author hongda.li
 */
@AutoService(WebHandler.class)
public class DeleteScriptHandler implements WebHandler {
    private static final String PATH = "/usl/admin/api/delete-script";

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        List<ScriptInfo> scriptInfo = this.parseToArray(ScriptInfo.class);

        USLRunner runner = WebHelper.RUNNER_THREAD_LOCAL.get();

        for (ScriptInfo info : scriptInfo) {
            File file = new File(ScriptSearcher.buildScriptPath(runner, info));
            FileUtil.del(file);
        }

        this.writeToJson(Returns.success());
    }
}
