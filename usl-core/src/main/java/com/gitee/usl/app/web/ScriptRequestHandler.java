package com.gitee.usl.app.web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.gitee.usl.UslRunner;
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
 *
 * @author hongda.li
 */
public class ScriptRequestHandler extends HttpServerHandler {
    private static final String SCRIPT_NAME = "script";
    private static final byte[] NONE_MATCHED = "The request path does not match, please check the request path.".getBytes(StandardCharsets.UTF_8);
    private final String path;
    private final UslRunner runner;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final TypeReference<Map<String, Object>> REFERENCE = new TypeReference<>() {
    };

    public ScriptRequestHandler(UslRunner runner) {
        this.runner = runner;
        this.path = runner.configuration().configWebServer().getPath();
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws Throwable {
        String api = request.getRequestURI();

        if (!path.equals(api)) {
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
                            .setScript(String.valueOf(context.remove(SCRIPT_NAME)))))
                    .getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.write(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }
}
