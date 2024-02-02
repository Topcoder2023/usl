package com.gitee.usl.function.ai.infra;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.gitee.usl.infra.exception.USLExecuteException;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author hongda.li
 */
public class JwtTokenHelper {
    private JwtTokenHelper() {
    }

    public static String generateToken(ChatType type, String secretKey, String id) {
        return switch (type) {
            case ZHI_PU_4 -> tokenFor100(secretKey, id);
            default -> throw new UnsupportedOperationException("无效的对话类型");
        };
    }

    private static String tokenFor100(String secretKey, String id) {
        return JWT.create()
                .setHeader("alg", "HS256")
                .setHeader("sign_type", "SIGN")
                .setPayload("api_key", id)
                .setPayload("timestamp", System.currentTimeMillis())
                .setPayload("exp", new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L))
                .sign(JWTSignerUtil.hs256(secretKey.getBytes(StandardCharsets.UTF_8)));
    }
}
