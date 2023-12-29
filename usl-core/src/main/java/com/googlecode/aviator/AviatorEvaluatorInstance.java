package com.googlecode.aviator;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.exception.UslException;
import com.googlecode.aviator.Options.Value;
import com.googlecode.aviator.asm.Opcodes;
import com.googlecode.aviator.code.CodeGenerator;
import com.googlecode.aviator.code.EvalCodeGenerator;
import com.googlecode.aviator.code.OptimizeCodeGenerator;
import com.googlecode.aviator.code.asm.ASMCodeGenerator;
import com.googlecode.aviator.exception.*;
import com.googlecode.aviator.lexer.ExpressionLexer;
import com.googlecode.aviator.lexer.SymbolTable;
import com.googlecode.aviator.lexer.token.CharToken;
import com.googlecode.aviator.lexer.token.OperatorType;
import com.googlecode.aviator.lexer.token.Token;
import com.googlecode.aviator.lexer.token.Token.TokenType;
import com.googlecode.aviator.lexer.token.Variable;
import com.googlecode.aviator.parser.AviatorClassLoader;
import com.googlecode.aviator.parser.ExpressionParser;
import com.googlecode.aviator.runtime.RuntimeFunctionDelegator;
import com.googlecode.aviator.runtime.function.seq.*;
import com.googlecode.aviator.runtime.function.seq.SeqCompsitePredFunFunction.LogicOp;
import com.googlecode.aviator.runtime.function.system.*;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.string.ExpressionSegment;
import com.googlecode.aviator.runtime.type.string.LiteralSegment;
import com.googlecode.aviator.runtime.type.string.StringSegment;
import com.googlecode.aviator.runtime.type.string.VarSegment;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.StringCharacterIterator;
import java.util.*;

/**
 * @author hongda.li
 */
@Slf4j
@Description("脚本引擎实例")
public final class AviatorEvaluatorInstance {

    @Getter
    @Setter
    @Description("前置后置处理器")
    private EnvProcessor envProcessor;

    @Getter
    @Setter
    @Description("函数加载器")
    private FunctionLoader functionLoader;

    @Getter
    @Setter
    @Description("函数兜底机制")
    private FunctionMissing functionMissing;

    @Getter
    @Setter
    @Description("字节码版本号")
    private int bytecodeVersion = Opcodes.V1_7;

    @Getter
    @Description("自定义类加载器")
    private final AviatorClassLoader classLoader;

    @Getter
    @Description("脚本引擎选项配置集合")
    private volatile Map<Options, Value> options = new IdentityHashMap<>();

    @Description("操作符别名映射关系")
    private final Map<OperatorType, String> aliasOperatorTokens = new IdentityHashMap<>();

    @Description("脚本引擎实例构造器")
    public AviatorEvaluatorInstance(final EvalMode evalMode) {
        for (Options opt : Options.values()) {
            this.options.put(opt, opt.getDefaultValueObject());
        }
        setOption(Options.EVAL_MODE, evalMode);
        loadFeatureFunctions();
        loadLib();
        this.classLoader = AccessController.doPrivileged((PrivilegedAction<AviatorClassLoader>) () -> new AviatorClassLoader(this.getClass().getClassLoader()));
    }

    @Description("为操作符函数建立别名")
    public void aliasOperator(final OperatorType type, final String token) {
        if ((type != OperatorType.AND && type != OperatorType.OR) || !ExpressionParser.isJavaIdentifier(token)) {
            throw new UslException("暂不支持的Token类型 - {}", type);
        }
        this.aliasOperatorTokens.put(type, token);
    }

    @Description("获取指定操作符的别名")
    public String getOperatorAliasToken(final OperatorType type) {
        return this.aliasOperatorTokens.get(type);
    }

    @Description("设置脚本引擎配置选项")
    public void setOption(final Options opt, final Object val) {
        if (opt == null || val == null) {
            throw new IllegalArgumentException("Option and value should not be null.");
        }
        if (!opt.isValidValue(val)) {
            throw new IllegalArgumentException("Invalid value for option:" + opt.name());
        }
        Map<Options, Value> newOpts = new IdentityHashMap<>(this.options);
        newOpts.put(opt, opt.intoValue(val));
        if (opt == Options.FEATURE_SET) {
            Set<Feature> oldSet = new HashSet<>(getFeatures());
            @SuppressWarnings("unchecked")
            Set<Feature> newSet = (Set<Feature>) val;
            if (oldSet.removeAll(newSet)) {
                // removed functions that feature is disabled.
                for (Feature feat : oldSet) {
                    for (AviatorFunction fn : feat.getFunctions()) {
                        this.removeSystemFunction(fn);
                    }
                }
            }
        }
        this.options = newOpts;
        if (opt == Options.FEATURE_SET) {
            loadFeatureFunctions();
        }
    }

    @Description("获取脚本引擎配置选项")
    public Value getOptionValue(final Options opt) {
        return this.options.get(opt);
    }

    @Description("获取脚本引擎语法特性配置集合")
    public Set<Feature> getFeatures() {
        return this.options.get(Options.FEATURE_SET).featureSet;
    }

    @Description("判断脚本引擎语法特性配置是否启用")
    public boolean isFeatureEnabled(final Feature feature) {
        return this.options.get(Options.FEATURE_SET).featureSet.contains(feature);
    }

    @Description("禁用脚本引擎语法特性配置")
    public void disableFeature(final Feature feature) {
        this.options.get(Options.FEATURE_SET).featureSet.remove(feature);
        for (AviatorFunction fn : feature.getFunctions()) {
            this.removeSystemFunction(fn);
        }
        loadFeatureFunctions();
    }

    @Getter
    private final Map<String, Object> systemFunctionMap = new HashMap<>();

    @Getter
    private final Map<OperatorType, AviatorFunction> operatorFunctionMap = new IdentityHashMap<>();

    private void loadLib() {
        loadSystemFunctions();

        loadSeqFunctions();
    }

    private void loadSeqFunctions() {
        addSystemFunction(SeqCollectorFunction.INSTANCE);
        addSystemFunction(SeqCollectorRawFunction.INSTANCE);
        addSystemFunction(SeqKeysFunction.INSTANCE);
        addSystemFunction(SeqValsFunction.INSTANCE);
        addSystemFunction(SeqReverseFunction.INSTANCE);
        addSystemFunction(SeqZipmapFunction.INSTANCE);
        addSystemFunction(new SeqNewArrayFunction());
        addSystemFunction(new SeqArrayFunction());
        addSystemFunction(new SeqNewListFunction());
        addSystemFunction(new SeqNewMapFunction());
        addSystemFunction(new SeqNewSetFunction());
        addSystemFunction(new SeqMapEntryFunction());
        addSystemFunction(new SeqIntoFunction());
        addSystemFunction(new SeqAddFunction());
        addSystemFunction(new SeqAddAllFunction());
        addSystemFunction(new SeqRemoveFunction());
        addSystemFunction(new SeqGetFunction());
        addSystemFunction(new SeqPutFunction());
        addSystemFunction(new SeqMinFunction());
        addSystemFunction(new SeqMaxFunction());
        addSystemFunction(new SeqMapFunction());
        addSystemFunction(new SeqReduceFunction());
        addSystemFunction(new SeqFilterFunction());
        addSystemFunction(new SeqSortFunction());
        addSystemFunction(new SeqIncludeFunction());
        addSystemFunction(new SeqContainsKeyFunction());
        addSystemFunction(new SeqCountFunction());
        addSystemFunction(new SeqEveryFunction());
        addSystemFunction(new SeqNotAnyFunction());
        addSystemFunction(new SeqSomeFunction());
        addSystemFunction(new SeqMakePredicateFunFunction("seq.eq", OperatorType.EQ));
        addSystemFunction(new SeqMakePredicateFunFunction("seq.neq", OperatorType.NEQ));
        addSystemFunction(new SeqMakePredicateFunFunction("seq.lt", OperatorType.LT));
        addSystemFunction(new SeqMakePredicateFunFunction("seq.le", OperatorType.LE));
        addSystemFunction(new SeqMakePredicateFunFunction("seq.gt", OperatorType.GT));
        addSystemFunction(new SeqMakePredicateFunFunction("seq.ge", OperatorType.GE));
        addSystemFunction(new SeqCompsitePredFunFunction("seq.and", LogicOp.AND));
        addSystemFunction(new SeqCompsitePredFunFunction("seq.or", LogicOp.OR));
        addSystemFunction(new SeqMakePredicateFunFunction("seq.true", OperatorType.EQ, AviatorBoolean.TRUE));
        addSystemFunction(
                new SeqMakePredicateFunFunction("seq.false", OperatorType.EQ, AviatorBoolean.FALSE));
        addSystemFunction(new SeqMakePredicateFunFunction("seq.nil", OperatorType.EQ, AviatorNil.NIL));
        addSystemFunction(new SeqMakePredicateFunFunction("seq.exists", OperatorType.NEQ, AviatorNil.NIL));
    }

    private void loadSystemFunctions() {
        addSystemFunction(ComparatorFunction.INSTANCE);
        addSystemFunction(new CompareFunction());
        addSystemFunction(new SysDateFunction());
        addSystemFunction(new PrintlnFunction());
        addSystemFunction(new PrintFunction());
        addSystemFunction(new PstFunction());
        addSystemFunction(new RandomFunction());
        addSystemFunction(new NowFunction());
        addSystemFunction(new LongFunction());
        addSystemFunction(new BooleanFunction());
        addSystemFunction(new DoubleFunction());
        addSystemFunction(new StrFunction());
        addSystemFunction(new BigIntFunction());
        addSystemFunction(new DecimalFunction());
        addSystemFunction(new Date2StringFunction());
        addSystemFunction(new String2DateFunction());
        addSystemFunction(new BinaryFunction(OperatorType.ADD));
        addSystemFunction(new BinaryFunction(OperatorType.Exponent));
        addSystemFunction(new BinaryFunction(OperatorType.SUB));
        addSystemFunction(new BinaryFunction(OperatorType.MULT));
        addSystemFunction(new BinaryFunction(OperatorType.DIV));
        addSystemFunction(new BinaryFunction(OperatorType.MOD));
        addSystemFunction(new BinaryFunction(OperatorType.NEG));
        addSystemFunction(new BinaryFunction(OperatorType.NOT));
        addSystemFunction(new BinaryFunction(OperatorType.BIT_AND));
        addSystemFunction(new BinaryFunction(OperatorType.BIT_OR));
        addSystemFunction(new BinaryFunction(OperatorType.BIT_XOR));
        addSystemFunction(new BinaryFunction(OperatorType.BIT_NOT));
        addSystemFunction(new TupleFunction());
        addSystemFunction(new MinFunction());
        addSystemFunction(new MaxFunction());
        addSystemFunction(new IdentityFunction());
        addSystemFunction(new AssertFunction());
        addSystemFunction(new RangeFunction());
        addSystemFunction(new IsDefFunction());
        addSystemFunction(new UndefFunction());
        addSystemFunction(new TypeFunction());
        addSystemFunction(SeqFunction.INSTANCE);
        addSystemFunction(IsAFunction.INSTANCE);
        addSystemFunction(MetaFunction.INSTANCE);
        addSystemFunction(WithMetaFunction.INSTANCE);
        addSystemFunction(WithoutMetaFunction.INSTANCE);
    }

    private void loadFeatureFunctions() {
        for (Feature feat : this.options.get(Options.FEATURE_SET).featureSet) {
            for (AviatorFunction fn : feat.getFunctions()) {
                if (!existsSystemFunction(fn.getName())) {
                    this.addSystemFunction(fn);
                }
            }
        }
    }

    /**
     * Returns classloader
     *
     * @return
     */
    public AviatorClassLoader getClassLoader(final boolean cached) {
        if (cached) {
            return this.classLoader;
        } else {
            return new AviatorClassLoader(this.getClass().getClassLoader());
        }
    }

    @Description("添加内置函数")
    public void addSystemFunction(final AviatorFunction function) {
        Assert.notNull(function, "函数实例不能为空");

        String name = function.getName();

        if (SymbolTable.isReservedKeyword(name)) {
            throw new UslException("函数名称与保留关键字冲突 - {}", name);
        }

        if (this.systemFunctionMap.containsKey(name)) {
            throw new UslException("已存在相同名称的函数 - {}", name);
        }

        this.systemFunctionMap.put(name, function);
        log.debug("注册系统内置函数 - [{}]", name);
    }

    @Description("尝试获取函数")
    public AviatorFunction getFunction(final String name) {
        return this.getFunction(name, null);
    }

    @Description("尝试获取函数")
    public AviatorFunction getFunction(final String name, final SymbolTable symbolTable) {
        Assert.notNull(functionLoader, "函数加载器尚未初始化");

        @Description("从内置函数中尝试获取函数")
        AviatorFunction function = (AviatorFunction) this.systemFunctionMap.get(name);
        if (function != null) {
            return function;
        }

        @Description("从函数加载器中尝试获取函数")
        AviatorFunction fromLoader = functionLoader.onFunctionNotFound(name);
        if (fromLoader != null) {
            return fromLoader;
        }

        @Description("从上下文中尝试获取函数并转为委托函数")
        AviatorFunction fromEnv = new RuntimeFunctionDelegator(name, symbolTable, this.functionMissing);
        return fromEnv;
    }

    @Description("重载操作符函数")
    public void addOpFunction(final OperatorType opType, final AviatorFunction function) {
        this.operatorFunctionMap.put(opType, function);
    }

    @Description("获取操作符函数实现")
    public AviatorFunction getOpFunction(final OperatorType opType) {
        return this.operatorFunctionMap.get(opType);
    }

    @Description("移除操作符函数")
    public AviatorFunction removeOpFunction(final OperatorType opType) {
        return this.operatorFunctionMap.remove(opType);
    }

    @Description("判断系统内置函数是否存在")
    public boolean existsSystemFunction(final String name) {
        return this.systemFunctionMap.containsKey(name);
    }

    @Description("移除系统内置函数")
    public void removeSystemFunction(final AviatorFunction function) {
        Assert.notNull(function, "函数实例不能为空");
        this.systemFunctionMap.remove(function.getName());
    }

    public CodeGenerator newCodeGenerator(final String sourceFile, final boolean cached) {
        AviatorClassLoader classLoader = getClassLoader(cached);
        return newCodeGenerator(classLoader, sourceFile);

    }

    public EvalCodeGenerator newEvalCodeGenerator(final AviatorClassLoader classLoader,
                                                  final String sourceFile) {
        return new ASMCodeGenerator(this, sourceFile, classLoader);
    }

    public CodeGenerator newCodeGenerator(final AviatorClassLoader classLoader,
                                          final String sourceFile) {
        return new OptimizeCodeGenerator(this, sourceFile, classLoader);
    }


    public void ensureFeatureEnabled(final Feature feature) {
        if (!getOptionValue(Options.FEATURE_SET).featureSet.contains(feature)) {
            throw new UnsupportedFeatureException(feature);
        }
    }

    public static class StringSegments {
        public final List<StringSegment> items;
        public int hintLength;

        public StringSegments(final List<StringSegment> items, final int hintLength) {
            super();
            this.items = items;
            this.hintLength = hintLength;
        }

        public boolean isEmpty() {
            return this.items.isEmpty();
        }

        public String toString(final Map<String, Object> env, final String lexeme) {
            if (this.items.isEmpty()) {
                return lexeme;
            }
            StringBuilder builder = new StringBuilder(this.hintLength);
            for (StringSegment item : this.items) {
                item.appendTo(builder, env);
            }
            final String result = builder.toString();
            final int newLen = result.length();
            if (newLen > this.hintLength && newLen < 10 * this.hintLength) {
                this.hintLength = newLen;
            }
            return result;
        }
    }

    /**
     * Compile a string to string segments, if string doesn't have a interpolation,returns an empty
     * list.
     *
     * @param lexeme
     * @return
     */
    public StringSegments compileStringSegments(final String lexeme) {
        return this.compileStringSegments(lexeme, null, 1);
    }

    /**
     * Compile a string to string segments, if string doesn't have a interpolation,returns an empty
     * list.
     *
     * @param lexeme
     * @param sourceFile
     * @param lineNo;
     * @return
     */
    public StringSegments compileStringSegments(final String lexeme, final String sourceFile,
                                                final int lineNo) {
        List<StringSegment> segs = new ArrayList<StringSegment>();
        boolean hasInterpolationOrEscaped = false;
        StringCharacterIterator it = new StringCharacterIterator(lexeme);
        char ch = it.current(), prev = StringCharacterIterator.DONE;
        int lastInterPos = 0;
        int i = 1;
        for (; ; ) {
            if (ch == '#') {
                if (prev == '\\') {
                    // # is escaped, skip the backslash.
                    final String segStr = lexeme.substring(lastInterPos, i - 2);
                    segs.add(new LiteralSegment(segStr));
                    lastInterPos = i - 1;
                    hasInterpolationOrEscaped = true;
                } else {
                    // # is not escaped.
                    prev = ch;
                    ch = it.next();
                    i++;
                    if (ch == '{') {
                        // Find a interpolation position.
                        if (i - 2 > lastInterPos) {
                            final String segStr = lexeme.substring(lastInterPos, i - 2);
                            segs.add(new LiteralSegment(segStr));
                        }

                        try {
                            ExpressionLexer lexer = new ExpressionLexer(this, lexeme.substring(i));
                            lexer.setLineNo(lineNo);
                            ExpressionParser parser =
                                    new ExpressionParser(this, lexer, newCodeGenerator(sourceFile, false));

                            Expression exp = parser.parse(false);
                            final Token<?> lookhead = parser.getLookhead();
                            if (lookhead == null || (lookhead.getType() != TokenType.Char
                                    || ((CharToken) lookhead).getCh() != '}')) {
                                parser.reportSyntaxError("expect '}' to complete string interpolation");
                            }
                            int expStrLen = lookhead.getStartIndex() + 1;
                            while (expStrLen-- > 0) {
                                prev = ch;
                                ch = it.next();
                                i++;
                            }
                            Token<?> previousToken = null;

                            if (parser.getParsedTokens() == 2 && (previousToken = parser.getPrevToken()) != null
                                    && previousToken.getType() == TokenType.Variable) {
                                // special case for inline variable.
                                if (previousToken == Variable.TRUE) {
                                    segs.add(new LiteralSegment("true"));
                                } else if (previousToken == Variable.FALSE) {
                                    segs.add(new LiteralSegment("false"));
                                } else if (previousToken == Variable.NIL) {
                                    segs.add(new LiteralSegment("null"));
                                } else {
                                    segs.add(new VarSegment(
                                            parser.getSymbolTable().reserve(previousToken.getLexeme()).getLexeme()));
                                }
                            } else {
                                segs.add(new ExpressionSegment(exp));
                            }
                            hasInterpolationOrEscaped = true;
                            lastInterPos = i;
                        } catch (Throwable t) {
                            throw new CompileExpressionErrorException(
                                    "Fail to compile string interpolation: " + lexeme, t);
                        }
                        // End of interpolation
                    }
                    // End of # is not escaped.
                }
            }

            if (ch == StringCharacterIterator.DONE) {
                if (i - 1 > lastInterPos) {
                    final String segStr = lexeme.substring(lastInterPos, i - 1);
                    segs.add(new LiteralSegment(segStr));
                }
                break;
            }

            prev = ch;
            ch = it.next();
            i++;
        }
        if (hasInterpolationOrEscaped) {
            return new StringSegments(segs, lexeme.length() * 2 / 3);
        } else {
            return new StringSegments(Collections.<StringSegment>emptyList(), 0);
        }
    }
}
