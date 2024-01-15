package com.gitee.usl.plugin.impl.stack;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.AsmConstants;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.grammar.utils.Env;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@Slf4j
@Description("函数调用栈")
public class CallStack {

    @Description("调用栈日志开始标识")
    private static final String START = "\n=====================================脚本调用栈=====================================\n";

    @Description("调用栈日志结束标识")
    private static final String END = "==================================================================================\n";

    @Description("调用栈日志分割标识")
    private static final String SPLIT = "----------------------------------------------------------------------------------\n";

    @Description("USL执行器-函数调用栈映射")
    private static final Map<String, ThreadLocal<Deque<FunctionSession>>> RUNNER_STACK = new ConcurrentHashMap<>(NumberConstant.COMMON_SIZE);

    @Description("入栈")
    public static void push(String runnerName, FunctionSession push) {

        @Description("调用栈")
        Deque<FunctionSession> deque = RUNNER_STACK.computeIfAbsent(runnerName, key -> ThreadLocal.withInitial(ArrayDeque::new)).get();

        deque.push(push);

        recordInfo(deque);
    }

    @Description("重置")
    public static void clear(String runnerName) {

        @Description("调用栈线程变量")
        ThreadLocal<Deque<FunctionSession>> local = RUNNER_STACK.get(runnerName);

        if (local != null) {
            log.debug("脚本执行结束，清空调用栈");
            local.remove();
        }
    }

    private static void recordInfo(@Description("调用栈") Deque<FunctionSession> deque) {

        @Description("当前调用会话")
        FunctionSession first = deque.getFirst();

        @Description("调用栈日志输出")
        StringBuilder stackInfo = new StringBuilder();

        first.getEnv()
                .entrySet()
                .stream()
                .filter(entry -> !AsmConstants.FUNC_PARAMS_VAR.equals(entry.getKey()))
                .filter(entry -> !"__fas__".equals(entry.getKey()))
                .forEach(entry -> stackInfo.append(entry.getKey())
                        .append(" => ")
                        .append(entry.getValue())
                        .append("\n")
                        .append(SPLIT));

        Optional.ofNullable(first.getInvocation()).ifPresent(invocation -> {
            stackInfo.append("函数类名 : ").append(invocation.targetType().getName()).append("\n").append(SPLIT);
            stackInfo.append("方法名称 : ").append(invocation.method().getName()).append("\n").append(SPLIT);
            stackInfo.append("函数名称 : ").append(first.getDefinition().getName()).append("\n").append(SPLIT);

            Optional.ofNullable(invocation.args()).ifPresent(args -> stackInfo.append("参数列表 : ")
                    .append(Stream.of(args)
                            .filter(arg -> arg == null || !Env.class.equals(arg.getClass()))
                            .map(ObjectUtil::toString)
                            .collect(Collectors.joining(CharPool.COMMA + CharSequenceUtil.SPACE)))
                    .append("\n").append(SPLIT));

            stackInfo.append("执行结果 : ").append(ObjectUtil.toString(first.getResult())).append("\n");
        });

        Optional.ofNullable(first.getException()).ifPresent(e -> {
            stackInfo.append(SPLIT);
            stackInfo.append("异常信息 : ").append(e.getMessage()).append("\n").append(SPLIT);
            stackInfo.append("异常栈 : ").append(ExceptionUtil.stacktraceToString(e)).append("\n");
        });

        if (stackInfo.length() > 0) {
            stackInfo.insert(0, START);
            stackInfo.append(END);
            log.debug(stackInfo.toString());
        }

    }
}
