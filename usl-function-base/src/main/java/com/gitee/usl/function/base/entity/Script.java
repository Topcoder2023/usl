package com.gitee.usl.function.base.entity;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;

/**
 * @author hongda.li
 */
public class Script {
    private final String content;
    private Object result;

    public Script(String path) {
        if (FileUtil.exist(path)) {
            this.content = new FileReader(path).readString();
        } else {
            this.content = null;
        }
    }

    public Object run(USLRunner runner) {
        try {
            this.result = runner.run(new Param().setScript(content).setCached(false)).getData();
        } catch (Exception e) {
            this.result = null;
        }
        return this.result;
    }

    public Object getResult() {
        return result;
    }
}
