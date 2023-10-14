package com.gitee.usl.kernel.engine;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.zhxu.okhttps.HTTP;
import cn.zhxu.okhttps.HttpResult;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.FuncClient;
import com.gitee.usl.kernel.domain.Result;

/**
 * @author hongda.li
 */
public class ClientFunction extends AnnotatedFunction {
    private static final long serialVersionUID = -5710593550227056668L;
    private final String script;
    private final String name;
    private final HTTP http;

    public ClientFunction(FunctionDefinition definition) {
        super(definition);
        FuncClient funcClient = AnnotationUtil.getAnnotation(definition.methodMeta().method(), FuncClient.class);
        this.name = funcClient.name();
        this.script = funcClient.script();
        this.http = USLRunner.findRunnerByName(funcClient.runnerName())
                .configuration()
                .configWebClient()
                .clientManager()
                .http();
    }

    @Override
    public Object handle(FunctionSession session) {
        HttpResult httpResult = http.async("/remote/call")
                .post()
                .getResult();
        Result<?> result = httpResult.getBody().toBean(Result.class);
        return result.getData();
    }
}
