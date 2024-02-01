package com.gitee.usl.resource.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.gitee.usl.infra.exception.USLException;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class ScriptInfo {
    private String scriptName;
    private String belongs;
    private String lastUpdatedTime;
    private String content;
    private Long fileSize;
    private String path;

    public void valid() {
        if (CharSequenceUtil.contains(belongs, StrPool.AT) || CharSequenceUtil.contains(scriptName, StrPool.AT)) {
            throw new USLException("脚本名称或功能分组不能包含 '@' 符号");
        }
    }

    public String buildName() {
        return belongs + StrPool.AT + scriptName;
    }

    public String getScriptName() {
        return scriptName;
    }

    public ScriptInfo setScriptName(String scriptName) {
        this.scriptName = scriptName;
        return this;
    }

    public String getBelongs() {
        return belongs;
    }

    public ScriptInfo setBelongs(String belongs) {
        this.belongs = belongs;
        return this;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public ScriptInfo setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ScriptInfo setContent(String content) {
        this.content = content;
        return this;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public ScriptInfo setFileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ScriptInfo setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ScriptInfo.class.getSimpleName() + "[", "]")
                .add("scriptName='" + scriptName + "'")
                .add("belongs='" + belongs + "'")
                .add("createTime='" + lastUpdatedTime + "'")
                .add("fileSize=" + fileSize)
                .toString();
    }
}
