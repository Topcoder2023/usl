package com.gitee.usl.resource.handler;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.resource.entity.Returns;
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
    private static final String PATH = "/usl/api/login";

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

        boolean bothIsEmpty = CharSequenceUtil.isEmpty(actual);
        if (Objects.equals(actual, "12346") || bothIsEmpty) {
            this.writeToJson(Returns.success(SecurityFilter.getTokenValue(runner.getName())));
        } else {
            this.writeToJson(Returns.failure("USL实例密码错误"));
        }
    }
}
