package com.gitee.usl.resource.filter;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.gitee.usl.USLRunner;
import com.gitee.usl.resource.api.FilterRoute;
import com.gitee.usl.resource.api.WebFilter;
import com.gitee.usl.resource.api.WebHelper;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.common.Cookie;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@FilterRoute({"/usl/admin/page/**", "/usl/admin/api/**"})
@AutoService(WebFilter.class)
public class SecurityFilter implements WebFilter {
    public static final String LOGIN_PAGE = "/usl/public/login";
    private static final String TOKEN_NAME = "access_token";
    private static final AES AES;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    static {
        AES = new AES(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()));
    }

    @Override
    public boolean doFilter(HttpRequest request, HttpResponse response) {
        String tokenValue = Stream.of(request.getCookies())
                .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(Optional.ofNullable(request.getHeader(TOKEN_NAME))
                        .orElse(request.getParameter(TOKEN_NAME)));

        final String runnerName;

        try {
            runnerName = AES.decryptStr(tokenValue);
        } catch (Exception e) {
            logger.warn("Access-Token解密失败 - {}", e.getMessage());
            this.redirect(LOGIN_PAGE);
            return false;
        }

        USLRunner runner = USLRunner.findRunnerByName(runnerName);
        if (runner == null) {
            logger.warn("无效的USL实例名称 - {}", runnerName);
            this.redirect(LOGIN_PAGE);
            return false;
        }

        WebHelper.RUNNER_THREAD_LOCAL.set(runner);
        logger.debug("USL实例认证通过 - {}", runnerName);
        return true;
    }

    public static String getTokenValue(String runnerName) {
        return AES.encryptBase64(runnerName);
    }
}
