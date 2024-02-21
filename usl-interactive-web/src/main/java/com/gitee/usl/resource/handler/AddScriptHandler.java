package com.gitee.usl.resource.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.ModuleConstant;
import com.gitee.usl.resource.ScriptSearcher;
import com.gitee.usl.resource.entity.Returns;
import com.gitee.usl.resource.api.WebHandler;
import com.gitee.usl.resource.api.WebHelper;
import com.gitee.usl.resource.entity.ScriptInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.io.File;

/**
 * @author hongda.li
 */
public class AddScriptHandler implements WebHandler {
    private static final String PATH = "/usl/admin/api/add-script";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        ScriptInfo scriptInfo = parseToObj(ScriptInfo.class);

        if (CharSequenceUtil.isBlank(scriptInfo.getBelongs())) {
            scriptInfo.setBelongs(ModuleConstant.DEFAULT);
        }

        USLRunner runner = WebHelper.RUNNER_THREAD_LOCAL.get();
        File file = new File(ScriptSearcher.buildScriptPath(runner, scriptInfo));
        FileWriter writer = new FileWriter(file);

        // 已经存在则删除原有文件，并重新写入
        if (FileUtil.exist(file)) {
            FileUtil.del(file);
        }

        logger.debug("开始写入脚本文件 - {}", writer.getFile().getAbsolutePath());

        try {
            scriptInfo.valid();
            writer.write(scriptInfo.getContent());
            this.writeToJson(Returns.success());
        } catch (Exception e) {
            logger.error("脚本文件写入失败 - {}", e.getMessage());
            this.writeToJson(Returns.failure(e.getMessage()));
        }
    }
}
