package com.gitee.usl.function.base.entity;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.kernel.domain.Param;
import com.googlecode.aviator.utils.Env;

import java.io.File;
import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class Script {
    private final USLRunner runner;
    private final String content;
    private final String path;
    private Object result;

    public Script(USLRunner runner, String path) {
        this.runner = runner;
        File file = new File(runner.configuration().configEngine().getScriptPath()
                + runner.name()
                + StrPool.SLASH
                + CharSequenceUtil.addSuffixIfNot(path, StrPool.DOT + StringConstant.SCRIPT_SUFFIX));
        if (FileUtil.exist(file)) {
            this.path = file.getAbsolutePath();
            this.content = new FileReader(file).readString();
        } else {
            this.path = null;
            this.content = null;
        }
    }

    public Object run(Env env) {
        if (content == null) {
            return null;
        }
        try {
            this.result = runner.run(new Param()
                            .setScript(content)
                            .setCached(false)
                            .setContext(env))
                    .getData();
        } catch (Exception e) {
            this.result = null;
        }
        return this.result;
    }

    public Object getResult(Env env) {
        if (result == null) {
            result = this.run(env);
        }
        return result;
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Script.class.getSimpleName() + "[", "]")
                .add("runner=" + runner.name())
                .add("path='" + path + "'")
                .toString();
    }
}
