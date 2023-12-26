package com.gitee.usl.function.base;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li
 */
@FunctionGroup
public class StringFunction {

    @Function
    public String trim(String str) {
        return CharSequenceUtil.trim(str);
    }

    @Function
    public String trimStart(String str) {
        return CharSequenceUtil.trimStart(str);
    }

    @Function
    public String trimEnd(String str) {
        return CharSequenceUtil.trimEnd(str);
    }

    @Function
    public String urlEncode(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, StandardCharsets.UTF_8.displayName());
    }

    @Function
    public String urlDecode(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, StandardCharsets.UTF_8.displayName());
    }
}
