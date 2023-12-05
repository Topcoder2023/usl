package com.gitee.usl.resource.handler;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.resource.ScriptSearcher;
import com.gitee.usl.resource.entity.Returns;
import com.gitee.usl.resource.api.WebHandler;
import com.google.auto.service.AutoService;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.Date;

/**
 * @author hongda.li
 */
@AutoService(WebHandler.class)
public class WelcomeHandler implements WebHandler {
    private static final String PATH = "/usl/admin/api/welcome";
    private static final String HELP_DOC_URL = "https://gitee.com/yixi-dlmu/usl/blob/master/README.md";
    private static final String SOURCE_URL_GITEE = "https://gitee.com/yixi-dlmu/usl";
    private Info infoCache;

    @Override
    public String getRoute() {
        return PATH;
    }

    @Override
    public void doHandle(HttpRequest request, HttpResponse response) {
        USLRunner runner = RUNNER_THREAD_LOCAL.get();

        Info info = this.getInfoCache(runner);

        info.runTime = DateUtil.formatBetween(runner.startTime(), new Date(), BetweenFormatter.Level.SECOND);
        info.scriptSize = ScriptSearcher.findCount();

        this.writeToJson(Returns.success(info));
    }

    private Info getInfoCache(USLRunner runner) {
        if (infoCache != null) {
            return infoCache;
        }
        this.infoCache = new Info();
        infoCache.name = runner.name();
        infoCache.helpDocUrl = HELP_DOC_URL;
        infoCache.version = StringConstant.VERSION;
        infoCache.sourceCodeUrl = SOURCE_URL_GITEE;
        infoCache.functionSize = runner.functions().size();
        infoCache.serviceSize = ServiceSearcher.searchAll(Initializer.class).size();
        infoCache.enhancerSize = ServiceSearcher.searchAll(FunctionEnhancer.class).size();
        return infoCache;
    }

    static final class Info {
        private String name;
        private String runTime;
        private String version;
        private String sourceCodeUrl;
        private String helpDocUrl;
        private Integer serviceSize;
        private Integer enhancerSize;
        private Integer functionSize;
        private Integer scriptSize;

        public String getName() {
            return name;
        }

        public Info setName(String name) {
            this.name = name;
            return this;
        }

        public String getRunTime() {
            return runTime;
        }

        public Info setRunTime(String runTime) {
            this.runTime = runTime;
            return this;
        }

        public String getVersion() {
            return version;
        }

        public Info setVersion(String version) {
            this.version = version;
            return this;
        }

        public String getSourceCodeUrl() {
            return sourceCodeUrl;
        }

        public Info setSourceCodeUrl(String sourceCodeUrl) {
            this.sourceCodeUrl = sourceCodeUrl;
            return this;
        }

        public String getHelpDocUrl() {
            return helpDocUrl;
        }

        public Info setHelpDocUrl(String helpDocUrl) {
            this.helpDocUrl = helpDocUrl;
            return this;
        }

        public Integer getEnhancerSize() {
            return enhancerSize;
        }

        public Info setEnhancerSize(Integer enhancerSize) {
            this.enhancerSize = enhancerSize;
            return this;
        }

        public Integer getFunctionSize() {
            return functionSize;
        }

        public Info setFunctionSize(Integer functionSize) {
            this.functionSize = functionSize;
            return this;
        }

        public Integer getScriptSize() {
            return scriptSize;
        }

        public Info setScriptSize(Integer scriptSize) {
            this.scriptSize = scriptSize;
            return this;
        }

        public Integer getServiceSize() {
            return serviceSize;
        }

        public Info setServiceSize(Integer serviceSize) {
            this.serviceSize = serviceSize;
            return this;
        }
    }
}
