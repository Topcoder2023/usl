package com.gitee.usl.kernel.loader;

import com.gitee.usl.api.FunctionLoader;
import com.gitee.usl.grammar.lexer.token.OperatorType;
import com.gitee.usl.grammar.runtime.function.internal.*;
import com.gitee.usl.grammar.runtime.function.system.BinaryFunction;
import com.gitee.usl.grammar.runtime.function.system.WithMetaFunction;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.kernel.engine.USLConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * 系统函数加载器
 *
 * @author hongda.li
 */
public class SystemFunctionLoader implements FunctionLoader {
    @Override
    public List<_Function> load(USLConfiguration configuration) {
        return Arrays.asList(
                new ReducerFunction(),
                new WithMetaFunction(),
                new IfCallccFunction(),
                new ReducerContFunction(),
                new ReducerBreakFunction(),
                new ReducerReturnFunction(),
                new BinaryFunction(OperatorType.ADD),
                new BinaryFunction(OperatorType.SUB),
                new BinaryFunction(OperatorType.DIV),
                new BinaryFunction(OperatorType.MOD),
                new BinaryFunction(OperatorType.NEG),
                new BinaryFunction(OperatorType.NOT),
                new BinaryFunction(OperatorType.MULT),
                new BinaryFunction(OperatorType.BIT_OR),
                new BinaryFunction(OperatorType.BIT_AND),
                new BinaryFunction(OperatorType.BIT_XOR),
                new BinaryFunction(OperatorType.BIT_NOT),
                new BinaryFunction(OperatorType.Exponent)
        );
    }
}
