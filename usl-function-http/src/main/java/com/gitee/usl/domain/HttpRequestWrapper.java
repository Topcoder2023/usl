package com.gitee.usl.domain;

import com.gitee.usl.api.annotation.Accessible;
import com.gitee.usl.infra.structure.wrapper.BaseWrapper;
import org.smartboot.http.common.utils.Mimetypes;
import org.smartboot.http.server.HttpRequest;

/**
 * @author hongda.li
 */
public class HttpRequestWrapper implements BaseWrapper<HttpRequest> {
    private HttpRequest httpRequest;

    @Accessible
    public String path() {
        return httpRequest.getRequestURI();
    }

    @Override
    public HttpRequest get() {
        return httpRequest;
    }

    @Override
    public HttpRequest set(HttpRequest value) {
        return this.httpRequest = value;
    }
}
