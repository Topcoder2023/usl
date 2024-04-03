package com.manda.usl.http.client.mapping;

import com.manda.usl.kernel.annotation.Singleton;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author hongda.li
 */
@Slf4j
@Singleton(whenNotExist = LoggerInterceptor.class)
public class LoggerInterceptor implements HttpInterceptor {
    @SuppressWarnings("NullableProblems")
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        log.debug("Http-Client request : [{} - {}]", request.method(), request.url());
        return chain.proceed(request);
    }
}
