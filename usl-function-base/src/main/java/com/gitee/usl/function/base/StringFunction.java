package com.gitee.usl.function.base;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li, jingshu.zeng
 */
@FunctionGroup
public class StringFunction {

    @Function("string_contains")
    public boolean contains(String text, String compareText) {
        return CharSequenceUtil.contains(text, compareText);
    }

    @Function("string_starts_with")
    public boolean starts_with(String arg1, String arg2) {
        return CharSequenceUtil.startWith(arg1, arg2);
    }

    @Function("string_ends_with")
    public boolean ends_with(String arg1, String arg2) {
        return CharSequenceUtil.endWith(arg1, arg2);
    }

    @Function("string_index_of")
    public int index_of(CharSequence arg1, char arg2) {
        return CharSequenceUtil.indexOf(arg1, arg2);
    }

    @Function("string_replace")
    public String replace(String text, String oldText, String newText) {
        return CharSequenceUtil.replace(text, oldText, newText);
    }

    @Function("string_substring")
    public String substring(String text, int start, int end) {
        return StrUtil.sub(text, start, end);
    }

    @Function("string_length")
    public int length(String text) {
        return CharSequenceUtil.length(text);
    }

    @Function("string_to_lower_case")
    public String to_lower_case(String text) {
        return text.toLowerCase();
    }

    @Function("string_regex")
    public int regex(String text, String regex) {
        return text.matches(regex) ? 1 : 0;
    }

    @Function("string_trim")
    public String trim(String str) {
        return CharSequenceUtil.trim(str);
    }

    @Function("string_trim_start")
    public String trim_start(String str) {
        return CharSequenceUtil.trimStart(str);
    }

    @Function("string_trim_end")
    public String trim_end(String str) {
        return CharSequenceUtil.trimEnd(str);
    }

    @Function("string_url_encode")
    public String url_encode(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, StandardCharsets.UTF_8.displayName());
    }

    @Function("string_url_decode")
    public String url_decode(String url) throws UnsupportedEncodingException {
        return URLDecoder.decode(url, StandardCharsets.UTF_8.displayName());
    }
}
