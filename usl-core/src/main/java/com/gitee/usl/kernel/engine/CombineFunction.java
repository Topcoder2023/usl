package com.gitee.usl.kernel.engine;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.UslExecuteException;
import com.gitee.usl.infra.structure.AttributeMeta;
import com.gitee.usl.infra.structure.StringList;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;

import java.util.Collections;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author hongda.li
 */
public class CombineFunction extends AnnotatedFunction {
    private static final long serialVersionUID = -3041831967451684737L;
    private final String script;
    private final transient USLRunner runner;
    private final transient StringList params;

    public CombineFunction(FunctionDefinition definition) {
        super(definition);
        AttributeMeta attribute = definition.attribute();
        this.script = attribute.search(StringConstant.SCRIPT_NAME, String.class);
        this.runner = attribute.search(StringConstant.RUNNER_NAME, USLRunner.class);
        this.params = attribute.search(StringConstant.PARAMS_NAME, StringList.class);
    }

    @Override
    public Object handle(FunctionSession session) {
        Object[] args = session.invocation().args();
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

        Param param = new Param()
                .setCached(true)
                .setScript(this.script)
                .setContext(context);

        Result<?> result = this.runner.run(param);
        ResultCode resultCode = ResultCode.of(result.getCode());
        Assert.isTrue(ResultCode.SUCCESS == resultCode, () -> new UslExecuteException(resultCode, result.getMessage()));

        return result.getData();
    }
}
