package com.gitee.usl.app.web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.kernel.domain.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;

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
public class ScriptRequestHandler extends HttpServerHandler {
    private static final byte[] NONE_MATCHED = "The request path does not match, please check the request path.".getBytes(StandardCharsets.UTF_8);
    private final USLRunner runner;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final TypeReference<Map<String, Object>> REFERENCE = new TypeReference<Map<String, Object>>() {
    };

    public ScriptRequestHandler(USLRunner runner) {
        this.runner = runner;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws Throwable {
        String api = request.getRequestURI();

        if (!StringConstant.REMOTE_CALL_PATH.equals(api)) {
            logger.debug("The request path does not match - {}", api);
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.write(NONE_MATCHED);
            return;
        }

        try {
            JSONObject parsed = JSON.parseObject(request.getInputStream(), StandardCharsets.UTF_8);
            Map<String, Object> context = parsed.to(REFERENCE);

            response.write(JSON.toJSONString(runner.run(new Param()
                            .setCached(false)
                            .setContext(context)
                            .setScript(String.valueOf(context.remove(StringConstant.SCRIPT_NAME)))))
                    .getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }
}
