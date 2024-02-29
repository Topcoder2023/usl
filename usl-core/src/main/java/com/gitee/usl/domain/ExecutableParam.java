package com.gitee.usl.domain;

import com.gitee.usl.USLRunner;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongda.li
 */
@Slf4j
@ToString
public class ExecutableParam extends Param {
    @Getter
    private final USLRunner runner;

    private Object result;

    public ExecutableParam(USLRunner runner, String content) {
        super(content);
        this.runner = runner;
    }

    public ExecutableParam(USLRunner runner, FileParam param) {
        super(param.getScript());
        this.runner = runner;
    }

    public ExecutableParam(USLRunner runner, ResourceParam param) {
        super(param.getScript());
        this.runner = runner;
    }

    public Object execute() {
        if (this.getScript() == null) {
            return null;
        }

        try {
            this.result = runner.run(this).getData();
        } catch (Exception e) {
            log.error("脚本执行失败", e);
            this.result = null;
        }

        return this.result;
    }

    public Object getCacheResult() {
        if (result == null) {
            result = this.execute();
        }

        return result;
    }

}
