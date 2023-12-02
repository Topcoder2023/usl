package com.gitee.usl.resource.filter;

import cn.hutool.core.util.IdUtil;
import com.gitee.usl.resource.api.WebFilter;
import com.google.auto.service.AutoService;
import org.smartboot.http.common.Cookie;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@AutoService(WebFilter.class)
public class SecurityFilter implements WebFilter {
    public static final String TOKEN_NAME = "access_token";
    private static final String LOGIN_PAGE = "usl/login";
    private static final String TOKEN_VALUE;

    static {
        TOKEN_VALUE = IdUtil.getSnowflakeNextIdStr();
    }

    @Override
    public String accept() {
        return "/usl/admin/api/**";
    }

    @Override
    public boolean doFilter(HttpRequest request, HttpResponse response) {
        String tokenValue = Stream.of(request.getCookies())
                .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(Optional.ofNullable(request.getHeader(TOKEN_NAME))
                        .orElse(request.getParameter(TOKEN_NAME)));

        if (TOKEN_VALUE.equals(tokenValue)) {
            return true;
        }

        this.redirect(LOGIN_PAGE);
        return false;
    }

    public static String getTokenValue() {
        return TOKEN_VALUE;
    }
}
