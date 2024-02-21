package com.gitee.usl.function.base;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
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

    @Function("contains")
    public boolean contains(String text, String compareText) {
        return CharSequenceUtil.contains(text, compareText);
    }

    @Function("starts_with")
    public boolean starts_with(String arg1, String arg2) {
        return CharSequenceUtil.startWith(arg1, arg2);
    }

    @Function("ends_with")
    public boolean ends_with(String arg1, String arg2) {
        return CharSequenceUtil.endWith(arg1, arg2);
    }

    @Function("index_of")
    public int index_of(CharSequence arg1, char arg2) {
        return CharSequenceUtil.indexOf(arg1, arg2);
    }

    @Function("replace")
    public String replace(String text, String oldText, String newText) {
        return CharSequenceUtil.replace(text, oldText, newText);
    }

    @Function("substring")
    public String substring(String text, int start, int end) {
        return StrUtil.sub(text, start, end);
    }

    @Function("length")
    public int length(String text) {
        return CharSequenceUtil.length(text);
    }


    @Function("to_lower_case")
    public String to_lower_case(String text) {
        return text.toLowerCase();
    }

    @Function("regex")
    public int regex(String text, String regex) {
        return text.matches(regex) ? 1 : 0;
    }

    @Function("pinyin")
    public String pinyin(String text) {
        return PinyinUtil.getPinyin(text, "");
    }

    @Function("trim")
    public String trim(String str) {
        return CharSequenceUtil.trim(str);
    }

    @Function("trim_start")
    public String trim_start(String str) {
        return CharSequenceUtil.trimStart(str);
    }

    @Function("trim_end")
    public String trim_end(String str) {
        return CharSequenceUtil.trimEnd(str);
    }

    @Function("url_encode")
    public String url_encode(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, StandardCharsets.UTF_8.displayName());
    }

    @Function("url_decode")
    public String url_decode(String url) throws UnsupportedEncodingException {
        return URLDecoder.decode(url, StandardCharsets.UTF_8.displayName());
    }
}
