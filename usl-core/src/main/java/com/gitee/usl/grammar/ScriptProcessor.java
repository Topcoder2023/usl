package com.gitee.usl.grammar;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.script.Script;

import java.util.Map;

/**
 * @author hongda.li
 */
public interface ScriptProcessor {

    @Description("执行脚本前的回调函数")
    void onBegin(Map<String, Object> env, Script script);

    @Description("执行脚本后的回调函数")
    void onAfter(Map<String, Object> env, Script script);

    @Description("空的回调函数")
    ScriptProcessor EMPTY = new ScriptProcessor() {
        @Override
        public void onBegin(Map<String, Object> env, Script script) {
            // do nothing
        }

        @Override
        public void onAfter(Map<String, Object> env, Script script) {
            // do nothing
        }
    };
}
