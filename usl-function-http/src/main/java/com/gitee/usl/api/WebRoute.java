package com.gitee.usl.api;

import com.gitee.usl.infra.structure.wrapper.BoolWrapper;
import lombok.Getter;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;

/**
 * @author hongda.li
 */
@Getter
public abstract class WebRoute {

    private final BoolWrapper filterFlag = new BoolWrapper(false);

    public void filter() {
        this.filterFlag.set(Boolean.TRUE);
    }

    public void handler() {
        this.filterFlag.set(Boolean.FALSE);
    }

    /**
     * 执行业务逻辑
     *
     * @param request  请求
     * @param response 响应
     * @return 若存在后续请求，是否继续执行
     */
    public abstract Boolean doHandle(HttpRequest request, HttpResponse response);
}
