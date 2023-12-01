package com.gitee.usl.resource;

import com.gitee.usl.app.web.WebFilter;
import com.google.auto.service.AutoService;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

import java.util.Optional;

/**
 * @author hongda.li
 */
@AutoService(WebFilter.class)
public class SecurityFilter implements WebFilter {
    private static final String TOKEN_NAME = "access_token";
    public static final String LOGIN_PAGE = "static/login";

    @Override
    public boolean doFilter(HttpRequest request, HttpResponse response) {

        // TODO 登录校验

        // this.redirect(LOGIN_PAGE);

        return true;
    }
}
