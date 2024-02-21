package com.gitee.usl.function.ai;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.function.ai.infra.ApiConfigHelper;
import com.gitee.usl.function.ai.infra.ChatType;
import com.gitee.usl.function.ai.infra.JwtTokenHelper;
import com.gitee.usl.infra.structure.StringMap;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * 清华智谱
 *
 * @author hongda.li
 */
@Slf4j
@FunctionGroup
public class ChatFunction {

    @Function("chat_100")
    public String chatFor100(String message) {
        StringMap<Object> data = new StringMap<>()
                .putOne("model", "glm-4")
                .putOne("stream", Boolean.FALSE)
                .putOne("temperature", 0.95F)
                .putOne("do_sample", Boolean.TRUE)
                .putOne("messages", Collections.singletonList(
                        new StringMap<>()
                                .putOne("role", "user")
                                .putOne("content", message)
                ));
        log.debug("对话大模型 [智谱4.0] 问 : {}", message);
        String key = ApiConfigHelper.getKey(ChatType.ZHI_PU_4);
        try (HttpResponse base = HttpRequest.post(ChatType.ZHI_PU_4.getPath())
                .timeout(1000 * 60 * 5)
                .header("Authorization", JwtTokenHelper.generateToken(ChatType.ZHI_PU_4,
                        key.split("\\.")[1],
                        key.split("\\.")[0]))
                .body(JSONUtil.toJsonStr(data))
                .execute()) {
            String answer = ((JSONObject) JSONUtil.parseObj(base.body())
                    .get("choices", JSONArray.class)
                    .getFirst())
                    .get("message", JSONObject.class)
                    .get("content", String.class);
            log.debug("对话大模型 [智谱4.0] 答 : {}", answer);
            return answer;
        }
    }
}
