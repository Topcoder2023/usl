package com.gitee.usl.resource.handler;

import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.resource.Returns;
import com.gitee.usl.resource.api.WebHandler;
import com.gitee.usl.resource.filter.SecurityFilter;
import com.google.auto.service.AutoService;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.Objects;

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
        String runnerName = this.getParam(StringConstant.RUNNER_NAME, String.class);

        USLRunner runner = USLRunner.findRunnerByName(runnerName);
        if (runner == null) {
            this.writeToJson(Returns.failure("USL实例不存在"));
            return;
        }

        String actual = this.getParam(StringConstant.PASSWORD, String.class);
        String accept = runner.configuration().configWebServer().getPassword();

        if (accept == null || Objects.equals(actual, accept)) {
            this.writeToJson(Returns.success(SecurityFilter.getTokenValue(runner.name())));
        } else {
            this.writeToJson(Returns.failure("USL实例密码错误"));
        }
    }
}
