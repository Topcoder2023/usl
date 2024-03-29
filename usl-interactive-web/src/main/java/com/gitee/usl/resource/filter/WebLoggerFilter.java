package com.gitee.usl.resource.filter;

import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.resource.api.WebFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
@Order(Integer.MIN_VALUE + 10)
public class WebLoggerFilter implements WebFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean doFilter(HttpRequest request, HttpResponse response) {
        logger.debug("Received request - {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }
}
