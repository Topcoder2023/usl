package com.gitee.usl.resource.handler;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.engine.ScriptEngineManager;
import com.gitee.usl.resource.Returns;
import com.gitee.usl.resource.api.WebHandler;
import com.google.auto.service.AutoService;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * @author hongda.li
 */
@AutoService(WebHandler.class)
public class WelcomeHandler implements WebHandler {
    private static final String PATH = "/usl/admin/api/welcome";
    private static final String HELP_DOC_URL = "https://gitee.com/yixi-dlmu/usl/blob/master/README.md";
    private static final String SOURCE_URL_GITEE = "https://gitee.com/yixi-dlmu/usl";
    private static final String SOURCE_URL_GITHUB = "https://github.com/Topcoder2023/usl";

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        USLRunner runner = RUNNER_THREAD_LOCAL.get();

        Info info = new Info();
        info.name = runner.name();
        info.helpDocUrl = HELP_DOC_URL;
        info.version = StringConstant.VERSION;
        info.minutes = DateUtil.between(runner.startTime(), new Date(), DateUnit.MINUTE);
        info.sourceCodeUrlOfGitee = SOURCE_URL_GITEE;
        info.sourceCodeUrlOfGithub = SOURCE_URL_GITHUB;
        info.functionSize = runner.functions().size();
        info.pluginSize = ScriptEngineManager.getPluginSet().size();
        info.runnerSize = USLRunner.findRunnerCount();
        info.scriptSize = Arrays.stream(FileUtil.ls(runner.configuration().configEngine().getScriptPath()))
                .filter(file -> Objects.equals(FileUtil.getSuffix(file), StringConstant.SCRIPT_SUFFIX))
                .count();

        this.writeToJson(Returns.success(info));
    }

    static final class Info {
        private String name;
        private Long minutes;
        private String version;
        private String sourceCodeUrlOfGitee;
        private String sourceCodeUrlOfGithub;
        private String helpDocUrl;
        private Integer runnerSize;
        private Integer pluginSize;
        private Integer functionSize;
        private Long scriptSize;

        public String getName() {
            return name;
        }

        public Info setName(String name) {
            this.name = name;
            return this;
        }

        public Long getMinutes() {
            return minutes;
        }

        public Info setMinutes(Long minutes) {
            this.minutes = minutes;
            return this;
        }

        public String getVersion() {
            return version;
        }

        public Info setVersion(String version) {
            this.version = version;
            return this;
        }

        public String getSourceCodeUrlOfGitee() {
            return sourceCodeUrlOfGitee;
        }

        public Info setSourceCodeUrlOfGitee(String sourceCodeUrlOfGitee) {
            this.sourceCodeUrlOfGitee = sourceCodeUrlOfGitee;
            return this;
        }

        public String getSourceCodeUrlOfGithub() {
            return sourceCodeUrlOfGithub;
        }

        public Info setSourceCodeUrlOfGithub(String sourceCodeUrlOfGithub) {
            this.sourceCodeUrlOfGithub = sourceCodeUrlOfGithub;
            return this;
        }

        public String getHelpDocUrl() {
            return helpDocUrl;
        }

        public Info setHelpDocUrl(String helpDocUrl) {
            this.helpDocUrl = helpDocUrl;
            return this;
        }

        public Integer getPluginSize() {
            return pluginSize;
        }

        public Info setPluginSize(Integer pluginSize) {
            this.pluginSize = pluginSize;
            return this;
        }

        public Integer getFunctionSize() {
            return functionSize;
        }

        public Info setFunctionSize(Integer functionSize) {
            this.functionSize = functionSize;
            return this;
        }

        public Long getScriptSize() {
            return scriptSize;
        }

        public Info setScriptSize(Long scriptSize) {
            this.scriptSize = scriptSize;
            return this;
        }

        public Integer getRunnerSize() {
            return runnerSize;
        }

        public Info setRunnerSize(Integer runnerSize) {
            this.runnerSize = runnerSize;
            return this;
        }
    }
}
