package com.gitee.usl.resource.handler;

import com.gitee.usl.USLRunner;
import com.gitee.usl.domain.Param;
import com.gitee.usl.domain.Result;
import com.gitee.usl.resource.ScriptSearcher;
import com.gitee.usl.resource.api.WebHandler;
import com.gitee.usl.resource.api.WebHelper;
import com.gitee.usl.resource.entity.Returns;
import com.gitee.usl.resource.entity.ScriptInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * 脚本请求处理器
 * 从参数中解析出脚本与脚本参数
 * 并调用 USL 执行器执行并返回
 * <br/>
 * 对于 SpringMVC 等自带 WEB 服务的项目可以直接定义相关接口
 * 并使接口支持脚本参数解析与执行即可
 *
 * @author hongda.li
 */
public class ScriptRequestHandler implements WebHandler {
    private static final String PATH = "/usl/admin/api/script/run";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        try {
            ScriptInfo scriptInfo = this.parseToObj(ScriptInfo.class);
            ScriptInfo find = ScriptSearcher.findOne(scriptInfo.getScriptName(), scriptInfo.getBelongs());

            if (logger.isDebugEnabled()) {
                logger.debug("收到脚本请求 : {}", scriptInfo.getScriptName());
            }

            USLRunner runner = WebHelper.RUNNER_THREAD_LOCAL.get();

            Result result = runner.run(new Param(find.getContent()));

            logger.debug("脚本计算完成 : {}", result);

            this.writeToJson(result);

        } catch (Exception e) {
            logger.error("脚本计算异常 : {}", e.getMessage());
            this.writeToJson(Returns.failure(e.getMessage()));
        }
    }
}
