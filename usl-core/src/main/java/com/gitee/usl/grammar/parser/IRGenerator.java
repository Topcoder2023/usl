package com.gitee.usl.grammar.parser;

import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.ScriptKeyword;
import com.gitee.usl.grammar.parser.ir.*;
import com.gitee.usl.grammar.script.Script;
import com.gitee.usl.grammar.info.MethodMetaData;
import com.gitee.usl.grammar.script.IE;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLCompileException;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.gitee.usl.grammar.lexer.token.OperatorType;
import com.gitee.usl.grammar.lexer.token.Token;
import com.gitee.usl.grammar.lexer.token.Token.TokenType;
import com.gitee.usl.grammar.runtime.FunctionArgument;
import com.gitee.usl.grammar.runtime.FunctionParam;
import com.gitee.usl.grammar.runtime.LambdaFunctionBootstrap;
import com.gitee.usl.grammar.utils.Constants;
import com.gitee.usl.grammar.utils.IdentityHashSet;

import java.util.*;

/**
 * @author hongda.li
 */
public class IRGenerator implements Generator {
    private static final Set<TokenType> LOAD_CONSTANTS_TYPE = new IdentityHashSet<>();
    private final ScriptEngine instance;
    private final Stack<Label> labels0 = new Stack<>();
    private final Stack<Label> labels1 = new Stack<>();
    private final List<IR> instruments = new ArrayList<>();
    private final List<FunctionArgument> functionArguments = new ArrayList<>();
    private final ArrayDeque<MethodMetaData> methodMetaDataStack = new ArrayDeque<>();
    private int labelNum;
    private Parser parser;
    private int funcInvocationId = 0;
    private ScriptKeyword symbolTable;
    private LambdaGenerator lambdaGenerator;
    private Generator parentCodeGenerator;
    private Map<String, LambdaFunctionBootstrap> lambdaBootstraps;

    static {
        LOAD_CONSTANTS_TYPE.add(TokenType.Number);
        LOAD_CONSTANTS_TYPE.add(TokenType.String);
        LOAD_CONSTANTS_TYPE.add(TokenType.Pattern);
        LOAD_CONSTANTS_TYPE.add(TokenType.Variable);
    }

    public IRGenerator(final ScriptEngine instance) {
        this.instance = instance;
    }

    @Override
    public void setParser(final Parser parser) {
        this.parser = parser;
        this.symbolTable = this.parser.getSymbolTable();
    }

    @Override
    public Script getResult(final boolean unboxObject) {
        Map<Label, Integer> labelMap = new IdentityHashMap<>();
        ListIterator<IR> it = instruments.listIterator();

        IntWrapper wrapper = new IntWrapper();
        while (it.hasNext()) {
            IR ir = it.next();
            if (ir instanceof VisitLabelIR) {
                it.remove();
                labelMap.put(((VisitLabelIR) ir).label(), wrapper.get());
            } else {
                wrapper.increment();
            }
        }

        instruments.stream()
                .filter(ir -> ir instanceof JumpIR)
                .forEach(ir -> ((JumpIR) ir).setPc(labelMap.get(((JumpIR) ir).getLabel())));

        final IE exp = new IE(this.instance, this.symbolTable, instruments, unboxObject);
        exp.setLambdaBootstraps(this.lambdaBootstraps);
        exp.setFunctionsArgs(this.functionArguments);

        return exp;
    }

    private void visitLabel(final Label label) {
        emit(new VisitLabelIR(label));
    }

    private void pushLabel0(final Label label) {
        this.labels0.push(label);
    }

    private Label popLabel0() {
        return this.labels0.pop();
    }

    private void pushLabel1(final Label label) {
        this.labels1.push(label);
    }

    private Label popLabel1() {
        return this.labels1.pop();
    }

    private Label peekLabel1() {
        return this.labels1.peek();
    }

    private Label makeLabel() {
        return new Label(this.labelNum++);
    }

    @Override
    public void genNewLambdaCode(final LambdaFunctionBootstrap bootstrap) {
        emit(new NewLambdaIR(bootstrap.getName()));
    }

    @Override
    public void onAssignment(final Token<?> headToken) {
        if (headToken.getMeta(Constants.DEFINE_META, false)) {
            emit(OperatorIR.DEF);
        } else {
            emit(OperatorIR.ASSIGN);
        }
    }

    @Override
    public void onShiftRight(final Token<?> headToken) {
        emit(OperatorIR.SHIFT_RIGHT);
    }

    @Override
    public void onShiftLeft(final Token<?> headToken) {
        emit(OperatorIR.SHIFT_LEFT);
    }

    @Override
    public void onUnsignedShiftRight(final Token<?> headToken) {
        emit(OperatorIR.UNSIGNED_SHIFT_RIGHT);
    }

    @Override
    public void onBitOr(final Token<?> headToken) {
        emit(OperatorIR.BIT_OR);
    }

    @Override
    public void onBitAnd(final Token<?> headToken) {
        emit(OperatorIR.BIT_AND);
    }

    @Override
    public void onBitXor(final Token<?> headToken) {
        emit(OperatorIR.BIT_XOR);
    }

    @Override
    public void onBitNot(final Token<?> headToken) {
        emit(OperatorIR.BIT_NOT);
    }

    @Override
    public void onAdd(final Token<?> headToken) {
        emit(OperatorIR.ADD);
    }

    @Override
    public void onSub(final Token<?> headToken) {
        emit(OperatorIR.SUB);
    }

    @Override
    public void onMult(final Token<?> headToken) {
        emit(OperatorIR.MULT);
    }

    @Override
    public void onExponent(final Token<?> token) {
        emit(OperatorIR.EXP);
    }

    @Override
    public void onDiv(final Token<?> headToken) {
        emit(OperatorIR.DIV);
    }

    @Override
    public void onAndLeft(final Token<?> headToken) {
    }

    @Override
    public void onAndRight(final Token<?> headToken) {
        emit(OperatorIR.AND);
    }

    @Override
    public void onTernaryBoolean(final Token<?> headToken) {
        Label label0 = makeLabel();
        pushLabel0(label0);
        Label label1 = makeLabel();
        pushLabel1(label1);
        this.instruments.add(new BranchUnlessIR(label0));
        emit(PopIR.INSTANCE);
    }

    @Override
    public void onTernaryLeft(final Token<?> headToken) {
        this.instruments.add(new GotoIR(peekLabel1()));
        Label label0 = popLabel0();
        visitLabel(label0);
        emit(PopIR.INSTANCE);
    }

    @Override
    public void onTernaryRight(final Token<?> headToken) {
        Label label1 = popLabel1();
        visitLabel(label1);
    }

    @Override
    public void onTernaryEnd(final Token<?> headToken) {
        emit(ClearIR.INSTANCE);
    }

    @Override
    public void onJoinLeft(final Token<?> headToken) {
    }

    @Override
    public void onJoinRight(final Token<?> headToken) {
        emit(OperatorIR.OR);
    }

    @Override
    public void onEq(final Token<?> headToken) {
        emit(OperatorIR.EQ);
    }

    @Override
    public void onMatch(final Token<?> headToken) {
        emit(OperatorIR.MATCH);
    }

    @Override
    public void onNeq(final Token<?> headToken) {
        emit(OperatorIR.NE);
    }

    @Override
    public void onLt(final Token<?> headToken) {
        emit(OperatorIR.LT);
    }

    @Override
    public void onLe(final Token<?> headToken) {
        emit(OperatorIR.LE);
    }

    @Override
    public void onGt(final Token<?> headToken) {
        emit(OperatorIR.GT);
    }

    @Override
    public void onGe(final Token<?> headToken) {
        emit(OperatorIR.GE);
    }

    @Override
    public void onMod(final Token<?> headToken) {
        emit(OperatorIR.MOD);
    }

    @Override
    public void onNot(final Token<?> headToken) {
        emit(OperatorIR.NOT);
    }

    @Override
    public void onNeg(final Token<?> headToken) {
        emit(OperatorIR.NEG);
    }

    @Override
    public void onConstant(final Token<?> headToken) {
        if (!LOAD_CONSTANTS_TYPE.contains(headToken.getType())) {
            return;
        }
        emit(new LoadIR(headToken));
    }

    @Override
    public void onMethodName(final Token<?> headToken) {
        final MethodMetaData metadata = new MethodMetaData(headToken, headToken.getType() == TokenType.Delegate ? null : headToken.getLexeme());
        this.methodMetaDataStack.push(metadata);
    }

    @Override
    public void onMethodParameter(final Token<?> headToken) {
        MethodMetaData currentMethodMetaData = this.methodMetaDataStack.peek();
        Optional.ofNullable(currentMethodMetaData).orElseThrow(() -> new USLCompileException(ResultCode.COMPILE_FAILURE));
        currentMethodMetaData.parameterCount++;
    }

    @Override
    public void onMethodInvoke(final Token<?> headToken) {
        final MethodMetaData methodMetaData = this.methodMetaDataStack.pop();

        @SuppressWarnings("unchecked")
        List<FunctionArgument> params = Optional.ofNullable(headToken)
                .map(token -> token.getMeta(Constants.PARAMS_META, Collections.EMPTY_LIST))
                .map(item -> (List<FunctionArgument>) item)
                .orElse(Collections.emptyList());

        int funcId = this.funcInvocationId++;
        this.functionArguments.addAll(params);
        methodMetaData.funcId = funcId;

        emit(new SendIR(methodMetaData.methodName,
                methodMetaData.parameterCount,
                methodMetaData.token.getMeta(Constants.UNPACK_ARGS, false),
                methodMetaData.funcId));
    }

    @Override
    public void onLambdaDefineStart(final Token<?> headToken) {
        Boolean newLexicalScope = headToken.getMeta(Constants.SCOPE_META, false);
        Boolean inheritEnv = headToken.getMeta(Constants.INHERIT_ENV_META, false);
        this.lambdaGenerator = new LambdaGenerator(this.instance, this, this.parser, newLexicalScope, inheritEnv);
        this.lambdaGenerator.setScopeInfo(this.parser.enterScope(newLexicalScope));
    }

    @Override
    public void onLambdaArgument(final Token<?> headToken, final FunctionParam param) {
        this.lambdaGenerator.addParam(param);
    }

    @Override
    public void onLambdaBodyStart(final Token<?> headToken) {
        this.parentCodeGenerator = this.parser.getCodeGenerator();
        this.parser.setCodeGenerator(this.lambdaGenerator);
    }

    @Override
    public void onLambdaBodyEnd(final Token<?> headToken) {
        LambdaFunctionBootstrap bootstrap = this.lambdaGenerator.getLambdaBootstrap();
        if (this.lambdaBootstraps == null) {
            this.lambdaBootstraps = new LinkedHashMap<>();
        }
        this.lambdaBootstraps.put(bootstrap.getName(), bootstrap);
        this.genNewLambdaCode(bootstrap);
        this.parser.restoreScope(this.lambdaGenerator.getScopeInfo());
        this.lambdaGenerator = null;
        this.parser.setCodeGenerator(this.parentCodeGenerator);
    }

    @Override
    public void onArray(final Token<?> headToken) {
        onConstant(headToken);
    }

    @Override
    public void onArrayIndexStart(final Token<?> token) {
    }

    @Override
    public void onArrayIndexEnd(final Token<?> headToken) {
        emit(OperatorIR.INDEX);
    }

    private void emit(IR ir) {
        if (ir instanceof OperatorIR operatorIR) {
            final OperatorType op = operatorIR.getOp();
            Optional.ofNullable(this.instance.getOpFunction(op))
                    .map(fn -> new OperatorIR(op, fn))
                    .ifPresentOrElse(this.instruments::add, () -> this.instruments.add(ir));
            return;
        }

        this.instruments.add(ir);
    }

}
