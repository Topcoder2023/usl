package com.gitee.usl.app.web;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.exception.UslNotFoundException;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.Map;

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
@AutoService(AbstractWebHandler.class)
public class ScriptRequestHandler extends AbstractWebHandler {
    private static final String PATH = "/remote/call";
    private final USLRunner runner;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ScriptRequestHandler() {
        this.runner = Singleton.get(StringConstant.RUNNER_NAME, () -> {
            throw new UslNotFoundException("USL Runner has not been initialized.");
        });
    }

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws Throwable {
        try {
            Map<String, Object> context = this.parseToMap(request);

            logger.debug("收到脚本请求 : {}", context);

            Result<?> result = runner.run(new Param()
                    .setCached(false)
                    .setContext(context)
                    .setScript(String.valueOf(context.remove(StringConstant.SCRIPT_NAME))));

            logger.debug("脚本计算完成 : {}", result);

            this.writeToJson(response, result);

        } catch (Exception e) {
            logger.error("脚本计算异常 : {}", e.getMessage());
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }
}
