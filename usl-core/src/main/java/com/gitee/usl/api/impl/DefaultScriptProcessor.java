package com.gitee.usl.api.impl;

import com.gitee.usl.api.ScriptProcessor;
import com.gitee.usl.grammar.script.Script;

import java.util.Map;

/**
 * @author hongda.li
 */
public class DefaultScriptProcessor implements ScriptProcessor {
    @Override
    public void onBegin(Map<String, Object> env, Script script) {
        // do nothing
    }

    @Override
    public void onAfter(Map<String, Object> env, Script script) {
        // do nothing
    }
}
