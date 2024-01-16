package com.gitee.usl.infra.structure;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.grammar.utils.Env;
import lombok.Getter;
import lombok.ToString;

import java.io.File;

/**
 * @author hongda.li
 */
@ToString
public class Script {

    private Object result;

    @Getter
    private final String path;

    @Getter
    private final String content;

    @Getter
    private final USLRunner runner;

    public Script(USLRunner runner, String path) {
        this.runner = runner;
        File file = new File(runner.configuration().getEngineConfig().getScriptPath()
                + runner.getName()
                + StrPool.SLASH
                + CharSequenceUtil.addSuffixIfNot(path, StrPool.DOT + StringConstant.SCRIPT_SUFFIX));
        if (FileUtil.exist(file)) {
            this.path = file.getAbsolutePath();
            this.content = new FileReader(file).readString();
        } else {
            this.path = path;
            this.content = null;
        }
    }

    public Object run(Env env) {
        if (content == null) {
            return null;
        }

        try {
            this.result = runner.run(new Param(content).addContext(env)).getData();
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

}
