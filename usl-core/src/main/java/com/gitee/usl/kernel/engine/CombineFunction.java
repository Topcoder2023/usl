package com.gitee.usl.kernel.engine;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.infra.structure.AttributeMeta;
import com.gitee.usl.infra.structure.StringList;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.Collections;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author hongda.li
 */
public class CombineFunction extends AnnotatedFunction {
    private final String script;
    private final transient USLRunner runner;
    private final transient StringList params;
    private final transient CompiledScript compiledScript;

    public CombineFunction(FunctionDefinition definition) {
        super(definition);
        AttributeMeta attribute = definition.getAttribute();
        this.script = attribute.getStr(StringConstant.SCRIPT_NAME);
        this.runner = attribute.getType(StringConstant.RUNNER_NAME, USLRunner.class);
        this.params = attribute.getType(StringConstant.PARAMS_NAME, StringList.class);
        this.compiledScript = attribute.getType(StringConstant.COMPILED_SCRIPT, CompiledScript.class);
    }

    @Override
    public Object handle(FunctionSession session) {
        Object[] args = session.getInvocation().args();
        Assert.isTrue(args.length == params.size(), "The expected parameter does not match the actual parameter number.");

        Map<String, Object> context;
        if (ArrayUtil.isEmpty(args)) {
            context = Collections.emptyMap();
        } else {
            context = MapUtil.newHashMap(args.length);
            IntStream.range(NumberConstant.ZERO, args.length)
                    .filter(i -> i < args.length)
                    .forEach(i -> context.put(params.get(i), args[i]));
        }

        if (compiledScript != null) {
            try {
                return compiledScript.eval(new SimpleBindings(context));
            } catch (ScriptException e) {
                throw new USLExecuteException(e);
            }
        }

        Param param = new Param().setCached(true).setScript(this.script).setContext(context);

        Result result = this.runner.run(param);
        ResultCode resultCode = ResultCode.of(result.getCode());
        Assert.isTrue(ResultCode.SUCCESS == resultCode, () -> new USLExecuteException(resultCode, result.getMessage()));

        return result.getData();
    }
}
