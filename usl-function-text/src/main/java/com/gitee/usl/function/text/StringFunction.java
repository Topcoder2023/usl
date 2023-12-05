package com.gitee.usl.function.text;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Func;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li
 */
@Func(StringFunctionProvider.STRING_FUNCTION_PREFIX)
public class StringFunction {

    @Func
    public String trim(String str) {
        return CharSequenceUtil.trim(str);
    }

    @Func
    public String trimStart(String str) {
        return CharSequenceUtil.trimStart(str);
    }

    @Func
    public String trimEnd(String str) {
        return CharSequenceUtil.trimEnd(str);
    }

    @Func
    public String urlEncode(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, StandardCharsets.UTF_8.displayName());
    }

    @Func
    public String urlDecode(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, StandardCharsets.UTF_8.displayName());
    }
}
