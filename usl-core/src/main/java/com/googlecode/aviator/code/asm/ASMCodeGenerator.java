package com.googlecode.aviator.code.asm;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrPool;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.AsmConstants;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.asm.CS;
import com.gitee.usl.grammar.asm.Script;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.asm.ClassWriter;
import com.googlecode.aviator.asm.Label;
import com.googlecode.aviator.asm.MethodVisitor;
import com.googlecode.aviator.asm.Opcodes;
import com.googlecode.aviator.code.BaseEvalCodeGenerator;
import com.googlecode.aviator.code.LambdaGenerator;
import com.googlecode.aviator.exception.CompileExpressionErrorException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.gitee.usl.grammar.ScriptKeyword;
import com.googlecode.aviator.lexer.token.NumberToken;
import com.googlecode.aviator.lexer.token.OperatorType;
import com.googlecode.aviator.lexer.token.Token;
import com.googlecode.aviator.lexer.token.Token.TokenType;
import com.googlecode.aviator.lexer.token.Variable;
import com.gitee.usl.grammar.asm.GlobalClassLoader;
import com.googlecode.aviator.parser.VariableMeta;
import com.googlecode.aviator.runtime.FunctionArgument;
import com.googlecode.aviator.runtime.FunctionParam;
import com.googlecode.aviator.runtime.LambdaFunctionBootstrap;
import com.googlecode.aviator.runtime.op.OperationRuntime;
import com.googlecode.aviator.utils.Constants;
import com.googlecode.aviator.utils.TypeUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.googlecode.aviator.asm.Opcodes.*;

/**
 * @author hongda.li
 */
@Slf4j
public class ASMCodeGenerator extends BaseEvalCodeGenerator {
    private static final String RUNTIME_UTILS = "com/googlecode/aviator/runtime/RuntimeUtils";
    private static final String OBJECT_DESC = "Lcom/googlecode/aviator/runtime/type/AviatorObject;";
    private static final String JAVA_TYPE_OWNER = "com/googlecode/aviator/runtime/type/AviatorJavaType";
    private static final String CONSTRUCTOR_METHOD_NAME = "<init>";
    private static final String OBJECT_OWNER = "com/googlecode/aviator/runtime/type/AviatorObject";
    public static final String FUNC_ARGS_INNER_VAR = "__fas__";
    private static final String FIELD_PREFIX = "f";
    private final ClassWriter classWriter;
    private MethodVisitor mv;
    private final String className;
    private static final AtomicLong CLASS_COUNTER = new AtomicLong();
    private int operandsCount = 0;
    private int maxStacks = 0;
    private int maxLocals = 2;
    private int fieldCounter = 0;

    private Map<String, String> innerVars = Collections.emptyMap();
    private Map<String, String> innerMethodMap = Collections.emptyMap();
    private Map<Token<?>, String> constantPool = Collections.emptyMap();
    private Map<String, Integer> methodTokens = Collections.emptyMap();
    private final Map<Label, Map<String, Integer>> labelNameIndexMap = new IdentityHashMap<>();
    private static final Label START_LABEL = new Label();
    private Label currentLabel = START_LABEL;

    private void setMaxStacks(final int newMaxStacks) {
        if (newMaxStacks > this.maxStacks) {
            this.maxStacks = newMaxStacks;
        }
    }

    public ASMCodeGenerator(final ScriptEngine instance,
                            final GlobalClassLoader classLoader) {
        super(instance, classLoader);
        this.className = AsmConstants.CLASS_NAME_PREFIX
                + DateUtil.format(new DateTime(), AsmConstants.DATETIME_FORMAT)
                + StrPool.UNDERLINE
                + CLASS_COUNTER.getAndIncrement();
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        log.debug("字节码构建开始，类名 - {}", this.className);
        visitClass();
    }

    @Override
    public void start() {
        makeConstructor();
        startVisitMethodCode();
    }

    private void startVisitMethodCode() {
        this.mv = this.classWriter.visitMethod(ACC_PUBLIC + +ACC_FINAL, "customImpl",
                "(Lcom/googlecode/aviator/utils/Env;)Ljava/lang/Object;",
                "(Lcom/googlecode/aviator/utils/Env;)Ljava/lang/Object;", null);
        this.mv.visitCode();
    }

    private void endVisitMethodCode(final boolean unboxObject) {
        if (this.operandsCount > 0) {
            loadEnv();
            if (unboxObject) {
                this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, "getValue",
                        "(Ljava/util/Map;)Ljava/lang/Object;");
            } else {
                this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, "deref",
                        "(Ljava/util/Map;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
            }
            this.mv.visitInsn(ARETURN);
            this.popOperand();
            this.popOperand();
        } else {
            this.mv.visitInsn(ACONST_NULL);
            this.mv.visitInsn(ARETURN);
            this.pushOperand();
            this.popOperand();
        }
        if (this.operandsCount > 0) {
            throw new CompileExpressionErrorException(
                    "operand stack is not empty,count=" + this.operandsCount);
        }
        this.mv.visitMaxs(this.maxStacks, this.maxLocals);
        this.mv.visitEnd();
    }

    private void endVisitClass() {
        this.classWriter.visitEnd();
    }

    /**
     * Make a default constructor
     */
    private void makeConstructor() {
        final String constructorDesc = AsmConstants.CLASS_DESC_LEFT
                + AsmConstants.SCRIPT_ENGINE_DESC
                + AsmConstants.CLASS_DESC_SPLIT
                + AsmConstants.LIST_DESC
                + AsmConstants.CLASS_DESC_SPLIT
                + AsmConstants.SYMBOL_TABLE_DESC
                + AsmConstants.CLASS_DESC_SPLIT
                + AsmConstants.CLASS_DESC_RIGHT
                + AsmConstants.CLASS_DESC_SUFFIX;

        this.mv = this.classWriter.visitMethod(ACC_PUBLIC, CONSTRUCTOR_METHOD_NAME, constructorDesc, null, null);
        this.mv.visitCode();
        this.mv.visitVarInsn(ALOAD, 0);
        this.mv.visitVarInsn(ALOAD, 1);
        this.mv.visitVarInsn(ALOAD, 2);
        this.mv.visitVarInsn(ALOAD, 3);
        this.mv.visitMethodInsn(INVOKESPECIAL, AsmConstants.EXPRESSION_CLASS_NAME, CONSTRUCTOR_METHOD_NAME, constructorDesc);
        if (!this.innerVars.isEmpty()) {
            for (Map.Entry<String, String> entry : this.innerVars.entrySet()) {
                String outterName = entry.getKey();
                String innerName = entry.getValue();
                this.mv.visitVarInsn(ALOAD, 0);
                this.mv.visitTypeInsn(NEW, JAVA_TYPE_OWNER);
                this.mv.visitInsn(DUP);
                this.mv.visitLdcInsn(outterName);
                this.mv.visitVarInsn(ALOAD, 3);
                this.mv.visitMethodInsn(INVOKESPECIAL, JAVA_TYPE_OWNER, CONSTRUCTOR_METHOD_NAME,
                        AsmConstants.CLASS_DESC_LEFT
                                + AsmConstants.STRING_DESC
                                + AsmConstants.CLASS_DESC_SPLIT
                                + AsmConstants.SYMBOL_TABLE_DESC
                                + AsmConstants.CLASS_DESC_SPLIT
                                + AsmConstants.CLASS_DESC_RIGHT
                                + AsmConstants.CLASS_DESC_SUFFIX);
                this.mv.visitFieldInsn(PUTFIELD, this.className, innerName,
                        "Lcom/googlecode/aviator/runtime/type/AviatorJavaType;");
            }
        }
        if (!this.innerMethodMap.isEmpty()) {
            for (Map.Entry<String, String> entry : this.innerMethodMap.entrySet()) {
                String outterName = entry.getKey();
                String innerName = entry.getValue();
                this.mv.visitVarInsn(ALOAD, 0);
                this.mv.visitVarInsn(ALOAD, 1);
                this.mv.visitLdcInsn(outterName);
                this.mv.visitVarInsn(ALOAD, 3);
                this.mv.visitMethodInsn(INVOKEVIRTUAL, AsmConstants.SCRIPT_ENGINE_PATH,
                        "getFunction",
                        "(Ljava/lang/String;" + AsmConstants.SYMBOL_TABLE_DESC + ";)Lcom/googlecode/aviator/runtime/type/AviatorFunction;");
                this.mv.visitFieldInsn(PUTFIELD, this.className, innerName,
                        "Lcom/googlecode/aviator/runtime/type/AviatorFunction;");
            }
        }

        if (!this.constantPool.isEmpty()) {
            for (Map.Entry<Token<?>, String> entry : this.constantPool.entrySet()) {
                Token<?> token = entry.getKey();
                String fieldName = entry.getValue();
                this.mv.visitVarInsn(ALOAD, 0);
                onConstant0(token, true);
                this.popOperand();
                this.mv.visitFieldInsn(PUTFIELD, this.className, fieldName, OBJECT_DESC);
            }
        }

        this.mv.visitInsn(RETURN);
        this.mv.visitMaxs(4, 1);
        this.mv.visitEnd();

    }

    @Description("通过字节码访问类")
    private void visitClass() {
        this.classWriter.visit(this.instance.getBytecodeVersion(),
                ACC_PUBLIC + ACC_SUPER,
                this.className,
                null,
                AsmConstants.EXPRESSION_CLASS_NAME,
                null);
        this.classWriter.visitSource(this.className, null);
    }

    /**
     * Make a label
     *
     * @return
     */
    private Label makeLabel() {
        return new Label();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.aviator.code.CodeGenerator#onAdd(com.googlecode.aviator .lexer.token.Token)
     */
    @Override
    public void onAdd(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.ADD, "add");
    }

    private void loadOpType(final OperatorType opType) {
        this.pushOperand();
        this.mv.visitFieldInsn(GETSTATIC, "com/googlecode/aviator/lexer/token/OperatorType",
                opType.name(), "Lcom/googlecode/aviator/lexer/token/OperatorType;");
    }

    /**
     * Pop a operand from stack
     */
    private void popOperand() {
        this.operandsCount--;
    }

    /**
     * Pop a operand from stack
     */
    private void popOperand(final int n) {
        this.operandsCount -= n;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.aviator.code.CodeGenerator#onSub(com.googlecode.aviator .lexer.token.Token)
     */
    @Override
    public void onSub(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.SUB, "sub");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.aviator.code.CodeGenerator#onMult(com.googlecode.aviator
     * .lexer.token.Token)
     */
    @Override
    public void onMult(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.MULT, "mult");
    }

    @Override
    public void onExponent(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.Exponent, "exponent");
    }

    @Override
    public void onAssignment(final Token<?> lookhead) {
        visitLineNumber(lookhead);
        OperatorType opType = lookhead.getMeta(Constants.DEFINE_META, false) ? OperatorType.DEFINE
                : OperatorType.ASSIGNMENT;
        loadEnv();
        if (!OperationRuntime.hasRuntimeContext(this.compileEnv, opType)) {
            String methodName = (opType == OperatorType.DEFINE) ? "defineValue" : "setValue";
            this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, methodName,
                    "(Lcom/googlecode/aviator/runtime/type/AviatorObject;Ljava/util/Map;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
        } else {
            loadOpType(opType);
            this.mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/aviator/runtime/op/OperationRuntime",
                    "eval",
                    "(Lcom/googlecode/aviator/runtime/type/AviatorObject;Lcom/googlecode/aviator/runtime/type/AviatorObject;Ljava/util/Map;Lcom/googlecode/aviator/lexer/token/OperatorType;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
            this.popOperand();
        }
        this.popOperand(3);
        this.pushOperand();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.aviator.code.CodeGenerator#onDiv(com.googlecode.aviator .lexer.token.Token)
     */
    @Override
    public void onDiv(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.DIV, "div");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.aviator.code.CodeGenerator#onMod(com.googlecode.aviator .lexer.token.Token)
     */
    @Override
    public void onMod(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.MOD, "mod");
    }

    /**
     * Do logic operation "&&" left operand
     */
    @Override
    public void onAndLeft(final Token<?> lookhead) {
        loadEnv();
        visitLeftBranch(lookhead, IFEQ, OperatorType.AND);
    }

    private void visitBoolean() {
        this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, "booleanValue", "(Ljava/util/Map;)Z");
    }

    private void pushLabel0(final Label l0) {
        this.l0stack.push(l0);
    }

    /**
     * Do logic operation "&&" right operand
     */
    @Override
    public void onAndRight(final Token<?> lookhead) {
        visitRightBranch(lookhead, IFEQ, OperatorType.AND);
        this.popOperand(2); // boolean object and environment
        this.pushOperand();
    }

    private void visitRightBranch(final Token<?> lookhead, final int ints,
                                  final OperatorType opType) {
        if (!OperationRuntime.hasRuntimeContext(this.compileEnv, opType)) {
            this.mv.visitInsn(DUP);
            loadEnv();
            String second = "FALSE";
            if (opType == OperatorType.OR) {
                second = "TRUE";
            }

            visitBoolean();
            this.mv.visitInsn(POP);

            Label l1 = makeLabel();
            visitLineNumber(lookhead);
            this.mv.visitJumpInsn(GOTO, l1);
            visitLabel(popLabel0());
            // Result is false
            this.mv.visitFieldInsn(GETSTATIC, "com/googlecode/aviator/runtime/type/AviatorBoolean",
                    second, "Lcom/googlecode/aviator/runtime/type/AviatorBoolean;");
            visitLabel(l1);
        } else {
            loadOpType(opType);
            visitLineNumber(lookhead);
            this.mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/aviator/runtime/op/OperationRuntime",
                    "eval",
                    "(Lcom/googlecode/aviator/runtime/type/AviatorObject;Ljava/util/Map;Lcom/googlecode/aviator/runtime/type/AviatorObject;Lcom/googlecode/aviator/lexer/token/OperatorType;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
            this.popOperand();
        }
    }

    /**
     * Label stack for ternary operator
     */
    private final Stack<Label> l0stack = new Stack<Label>();
    private final Stack<Label> l1stack = new Stack<Label>();

    @Override
    public void onTernaryBoolean(final Token<?> lookhead) {
        loadEnv();
        visitLineNumber(lookhead);
        visitBoolean();
        Label l0 = makeLabel();
        Label l1 = makeLabel();
        pushLabel0(l0);
        pushLabel1(l1);
        this.mv.visitJumpInsn(IFEQ, l0);
        this.popOperand();
        this.popOperand();
        this.pushOperand(2); // add two booleans

        this.popOperand(); // pop the last result
    }

    private void pushLabel1(final Label l1) {
        this.l1stack.push(l1);
    }

    @Override
    public void onTernaryLeft(final Token<?> lookhead) {
        this.mv.visitJumpInsn(GOTO, peekLabel1());
        visitLabel(popLabel0());
        visitLineNumber(lookhead);
        this.popOperand(); // pop one boolean
    }

    private Label peekLabel1() {
        return this.l1stack.peek();
    }

    @Override
    public void onTernaryRight(final Token<?> lookhead) {
        visitLabel(popLabel1());
        visitLineNumber(lookhead);
        this.popOperand(); // pop one boolean
    }

    @Override
    public void onTernaryEnd(final Token<?> lookhead) {
        visitLineNumber(lookhead);
        if (this.operandsCount == 0) {
            return;
        }
        while (--this.operandsCount > 0) {
            this.mv.visitInsn(POP);
        }
    }

    private Label popLabel1() {
        return this.l1stack.pop();
    }

    /**
     * Do logic operation "||" right operand
     */
    @Override
    public void onJoinRight(final Token<?> lookhead) {
        visitRightBranch(lookhead, IFNE, OperatorType.OR);
        this.popOperand(2);
        this.pushOperand();

    }

    private void visitLabel(final Label label) {
        this.mv.visitLabel(label);
        this.currentLabel = label;
    }

    private Label peekLabel0() {
        return this.l0stack.peek();
    }

    private Label popLabel0() {
        return this.l0stack.pop();
    }

    /**
     * Do logic operation "||" left operand
     */
    @Override
    public void onJoinLeft(final Token<?> lookhead) {
        loadEnv();
        visitLeftBranch(lookhead, IFNE, OperatorType.OR);
    }

    private void visitLeftBranch(final Token<?> lookhead, final int ints, final OperatorType opType) {
        if (!OperationRuntime.hasRuntimeContext(this.compileEnv, opType)) {
            visitBoolean();
            Label l0 = makeLabel();
            pushLabel0(l0);
            visitLineNumber(lookhead);
            this.mv.visitJumpInsn(ints, l0);
            this.popOperand();
        }
        this.popOperand();
    }

    @Override
    public void onEq(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFNE, OperatorType.EQ);
    }

    @Override
    public void onMatch(final Token<?> lookhead) {
        visitLineNumber(lookhead);
        visitBinOperator(lookhead, OperatorType.MATCH, "match");
        this.popOperand();
        this.pushOperand();
    }

    @Override
    public void onNeq(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFEQ, OperatorType.NEQ);
    }

    private void doCompareAndJump(final Token<?> lookhead, final int ints,
                                  final OperatorType opType) {
        visitLineNumber(lookhead);
        loadEnv();
        visitCompare(ints, opType);
        this.popOperand();
        this.popOperand();
    }

    private boolean isEqNe(final int ints) {
        return ints == IFEQ || ints == IFNE;
    }

    private void visitCompare(final int ints, final OperatorType opType) {
        if (!OperationRuntime.hasRuntimeContext(this.compileEnv, opType)) {
            this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, isEqNe(ints) ? "compareEq" : "compare",
                    "(Lcom/googlecode/aviator/runtime/type/AviatorObject;Ljava/util/Map;)I");
            Label l0 = makeLabel();
            Label l1 = makeLabel();
            this.mv.visitJumpInsn(ints, l0);
            this.mv.visitFieldInsn(GETSTATIC, "com/googlecode/aviator/runtime/type/AviatorBoolean",
                    "TRUE", "Lcom/googlecode/aviator/runtime/type/AviatorBoolean;");
            this.mv.visitJumpInsn(GOTO, l1);
            visitLabel(l0);
            this.mv.visitFieldInsn(GETSTATIC, "com/googlecode/aviator/runtime/type/AviatorBoolean",
                    "FALSE", "Lcom/googlecode/aviator/runtime/type/AviatorBoolean;");
            visitLabel(l1);
        } else {
            loadOpType(opType);
            this.mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/aviator/runtime/op/OperationRuntime",
                    "eval",
                    "(Lcom/googlecode/aviator/runtime/type/AviatorObject;Lcom/googlecode/aviator/runtime/type/AviatorObject;Ljava/util/Map;Lcom/googlecode/aviator/lexer/token/OperatorType;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
            this.popOperand();
        }

    }

    @Override
    public void onGe(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFLT, OperatorType.GE);
    }

    @Override
    public void onGt(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFLE, OperatorType.GT);
    }

    @Override
    public void onLe(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFGT, OperatorType.LE);

    }

    @Override
    public void onLt(final Token<?> lookhead) {
        doCompareAndJump(lookhead, IFGE, OperatorType.LT);
    }

    private void pushOperand(final int delta) {
        this.operandsCount += delta;
        setMaxStacks(this.operandsCount);
    }

    /**
     * Logic operation '!'
     */
    @Override
    public void onNot(final Token<?> lookhead) {
        visitUnaryOperator(lookhead, OperatorType.NOT, "not");
    }

    private void visitBinOperator(final Token<?> token, final OperatorType opType,
                                  final String methodName) {
        visitLineNumber(token);
        if (!OperationRuntime.hasRuntimeContext(this.compileEnv, opType)) {
            // swap arguments for regular-expression match operator.
            if (opType == OperatorType.MATCH) {
                this.mv.visitInsn(SWAP);
            }
            loadEnv();
            this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, methodName,
                    "(Lcom/googlecode/aviator/runtime/type/AviatorObject;Ljava/util/Map;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
        } else {
            loadEnv();
            loadOpType(opType);
            this.mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/aviator/runtime/op/OperationRuntime",
                    "eval",
                    "(Lcom/googlecode/aviator/runtime/type/AviatorObject;Lcom/googlecode/aviator/runtime/type/AviatorObject;Ljava/util/Map;Lcom/googlecode/aviator/lexer/token/OperatorType;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
            this.popOperand();
        }
        this.popOperand();
        this.popOperand();
    }

    private void visitLineNumber(final Token<?> token) {
        if (token != null && token.getLineNo() > 0) {
            this.mv.visitLineNumber(token.getLineNo(), this.currentLabel);
        }
    }

    private void visitUnaryOperator(final Token<?> lookhead, final OperatorType opType,
                                    final String methodName) {
        visitLineNumber(lookhead);
        this.mv.visitTypeInsn(CHECKCAST, OBJECT_OWNER);
        loadEnv();

        if (!OperationRuntime.hasRuntimeContext(this.compileEnv, opType)) {
            this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, methodName,
                    "(Ljava/util/Map;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
        } else {
            loadOpType(opType);
            this.mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/aviator/runtime/op/OperationRuntime",
                    "eval",
                    "(Lcom/googlecode/aviator/runtime/type/AviatorObject;Ljava/util/Map;Lcom/googlecode/aviator/lexer/token/OperatorType;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
            this.popOperand();
        }

        this.popOperand();
    }

    /**
     * Bit operation '~'
     */
    @Override
    public void onBitNot(final Token<?> lookhead) {
        visitUnaryOperator(lookhead, OperatorType.BIT_NOT, "bitNot");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.aviator.code.CodeGenerator#onNeg(com.googlecode.aviator .lexer.token.Token,
     * int)
     */
    @Override
    public void onNeg(final Token<?> lookhead) {
        visitUnaryOperator(lookhead, OperatorType.NEG, "neg");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.aviator.code.CodeGenerator#getResult()
     */
    @Override
    public Script getResult(final boolean unboxObject) {
        end(unboxObject);

        byte[] bytes = this.classWriter.toByteArray();
        try {
            Class<?> defineClass = ClassDefiner.defineClass(this.className, Script.class, bytes, this.classLoader);
            Constructor<?> constructor = defineClass.getConstructor(ScriptEngine.class, List.class, ScriptKeyword.class);
            CS exp = (CS) constructor.newInstance(this.instance, new ArrayList<>(this.variables.values()), this.symbolTable);
            exp.setLambdaBootstraps(this.lambdaBootstraps);
            exp.setFunctionsArgs(this.funcsArgs);
            return exp;
        } catch (ExpressionRuntimeException e) {
            throw e;
        } catch (Throwable e) {
            if (e.getCause() instanceof ExpressionRuntimeException) {
                throw (ExpressionRuntimeException) e.getCause();
            }
            throw new CompileExpressionErrorException("define class error", e);
        }
    }


    private void end(final boolean unboxObject) {
        endVisitMethodCode(unboxObject);
        endVisitClass();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.aviator.code.CodeGenerator#onConstant(com.googlecode.aviator
     * .lexer.token.Token)
     */
    @Override
    public void onConstant(final Token<?> lookhead) {
        onConstant0(lookhead, false);
    }

    private void onConstant0(final Token<?> lookhead, final boolean inConstructor) {
        if (lookhead == null) {
            return;
        }
        visitLineNumber(lookhead);
        // load token to stack
        switch (lookhead.getType()) {
            case Number:
                if (loadConstant(lookhead, inConstructor)) {
                    return;
                }

                // load numbers
                NumberToken numberToken = (NumberToken) lookhead;
                Number number = numberToken.getNumber();

                if (TypeUtils.isBigInt(number)) {
                    this.mv.visitLdcInsn(numberToken.getLexeme());
                    this.mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/aviator/runtime/type/AviatorBigInt",
                            "valueOf", "(Ljava/lang/String;)Lcom/googlecode/aviator/runtime/type/AviatorBigInt;");
                } else if (TypeUtils.isDecimal(number)) {
                    loadEnv();
                    // this.pushOperand();
                    this.mv.visitLdcInsn(numberToken.getLexeme());
                    String methodDesc =
                            "(Ljava/util/Map;Ljava/lang/String;)Lcom/googlecode/aviator/runtime/type/AviatorDecimal;";

                    if (inConstructor) {
                        methodDesc =
                                "(Lcom/gitee/usl/grammar/ScriptEngine;Ljava/lang/String;)Lcom/googlecode/aviator/runtime/type/AviatorDecimal;";
                    }
                    this.mv.visitMethodInsn(INVOKESTATIC,
                            "com/googlecode/aviator/runtime/type/AviatorDecimal", "valueOf", methodDesc);
                    this.popOperand();
                } else if (TypeUtils.isDouble(number)) {
                    this.mv.visitLdcInsn(number);
                    this.mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/aviator/runtime/type/AviatorDouble",
                            "valueOf", "(D)Lcom/googlecode/aviator/runtime/type/AviatorDouble;");
                } else {
                    this.mv.visitLdcInsn(number);
                    this.mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/aviator/runtime/type/AviatorLong",
                            "valueOf", "(J)Lcom/googlecode/aviator/runtime/type/AviatorLong;");
                }
                this.pushOperand();
                // this.popOperand();
                // this.popOperand();
                break;
            case String:
                if (loadConstant(lookhead, inConstructor)) {
                    return;
                }
                // load string
                this.mv.visitTypeInsn(NEW, "com/googlecode/aviator/runtime/type/AviatorString");
                this.mv.visitInsn(DUP);
                this.mv.visitLdcInsn(lookhead.getValue(null));
                this.mv.visitLdcInsn(true);
                this.mv.visitLdcInsn(lookhead.getMeta(Constants.INTER_META, true));
                this.mv.visitLdcInsn(lookhead.getLineNo());
                this.mv.visitMethodInsn(INVOKESPECIAL, "com/googlecode/aviator/runtime/type/AviatorString",
                        CONSTRUCTOR_METHOD_NAME, "(Ljava/lang/String;ZZI)V");
                this.pushOperand(6);
                this.popOperand(5);
                break;
            case Pattern:
                if (loadConstant(lookhead, inConstructor)) {
                    return;
                }
                // load pattern
                this.mv.visitTypeInsn(NEW, "com/googlecode/aviator/runtime/type/AviatorPattern");
                this.mv.visitInsn(DUP);
                this.mv.visitLdcInsn(lookhead.getValue(null));
                this.mv.visitMethodInsn(INVOKESPECIAL, "com/googlecode/aviator/runtime/type/AviatorPattern",
                        CONSTRUCTOR_METHOD_NAME, "(Ljava/lang/String;)V");
                this.pushOperand(3);
                this.popOperand(2);
                break;
            case Variable:
                // load variable
                Variable variable = (Variable) lookhead;

                if (variable.equals(Variable.TRUE)) {
                    this.mv.visitFieldInsn(GETSTATIC, "com/googlecode/aviator/runtime/type/AviatorBoolean",
                            "TRUE", "Lcom/googlecode/aviator/runtime/type/AviatorBoolean;");
                    this.pushOperand();
                } else if (variable.equals(Variable.FALSE)) {
                    this.mv.visitFieldInsn(GETSTATIC, "com/googlecode/aviator/runtime/type/AviatorBoolean",
                            "FALSE", "Lcom/googlecode/aviator/runtime/type/AviatorBoolean;");
                    this.pushOperand();
                } else if (variable.equals(Variable.NIL)) {
                    this.mv.visitFieldInsn(GETSTATIC, "com/googlecode/aviator/runtime/type/AviatorNil", "NIL",
                            "Lcom/googlecode/aviator/runtime/type/AviatorNil;");
                    this.pushOperand();
                } else {
                    String outterVarName = variable.getLexeme();
                    String innerVarName = this.innerVars.get(outterVarName);
                    if (innerVarName != null) {
                        // Is it stored in local?
                        Map<String, Integer> name2Index = this.labelNameIndexMap.get(this.currentLabel);
                        if (name2Index != null && name2Index.get(innerVarName) != null) {
                            int localIndex = name2Index.get(innerVarName);
                            this.mv.visitVarInsn(ALOAD, localIndex);
                            this.pushOperand();
                        } else {
                            // Get field at first time
                            this.mv.visitVarInsn(ALOAD, 0);
                            this.mv.visitFieldInsn(GETFIELD, this.className, innerVarName,
                                    "Lcom/googlecode/aviator/runtime/type/AviatorJavaType;");
                            // Variable is used more than once,store it to local
                            if (this.variables.get(outterVarName).getRefs() > 1) {
                                this.mv.visitInsn(DUP);
                                int localIndex = getLocalIndex();
                                this.mv.visitVarInsn(ASTORE, localIndex);
                                if (name2Index == null) {
                                    name2Index = new HashMap<>();
                                    this.labelNameIndexMap.put(this.currentLabel, name2Index);
                                }
                                name2Index.put(innerVarName, localIndex);
                                this.pushOperand(3);
                                this.popOperand(2);
                            } else {
                                this.pushOperand(2);
                                this.popOperand();
                            }
                        }

                    } else {
                        this.mv.visitTypeInsn(NEW, JAVA_TYPE_OWNER);
                        this.mv.visitInsn(DUP);
                        this.mv.visitLdcInsn(outterVarName);
                        this.mv.visitMethodInsn(INVOKESPECIAL, JAVA_TYPE_OWNER, CONSTRUCTOR_METHOD_NAME,
                                "(Ljava/lang/String;)V");
                        this.pushOperand(3);
                        this.popOperand(2);
                    }

                }
                break;
        }
    }

    private boolean loadConstant(final Token<?> lookhead, final boolean inConstructor) {
        String fieldName;
        if (!inConstructor && (fieldName = this.constantPool.get(lookhead)) != null) {
            this.mv.visitVarInsn(ALOAD, 0);
            this.mv.visitFieldInsn(GETFIELD, this.className, fieldName, OBJECT_DESC);
            this.pushOperand();
            return true;
        }
        return false;
    }

    @Override
    public void initVariables(final Map<String, VariableMeta/* counter */> vars) {
        this.variables = vars;
        this.innerVars = new HashMap<>(this.variables.size());
        for (String outterVarName : this.variables.keySet()) {
            if (outterVarName.equals(Constants.REDUCER_EMPTY_VAR)) {
                continue;
            }
            // Use inner variable name instead of outter variable name
            String innerVarName = getInnerName(outterVarName);
            this.innerVars.put(outterVarName, innerVarName);
            this.classWriter.visitField(ACC_PRIVATE, innerVarName,
                    "Lcom/googlecode/aviator/runtime/type/AviatorJavaType;", null, null).visitEnd();

        }
    }

    /**
     * Initial constant pool.
     *
     * @param constants
     */
    @Override
    public void initConstants(final Set<Token<?>> constants) {
        if (constants.isEmpty()) {
            return;
        }
        this.constantPool = new HashMap<>(constants.size());

        for (Token<?> token : constants) {
            String fieldName = getInnerName(token.getLexeme());
            this.constantPool.put(token, fieldName);
            this.classWriter.visitField(ACC_PRIVATE, fieldName, OBJECT_DESC, null, null).visitEnd();
        }
    }

    @Override
    public void initMethods(final Map<String, Integer/* counter */> methods) {
        this.methodTokens = methods;
        this.innerMethodMap = new HashMap<>(methods.size());
        for (String outterMethodName : methods.keySet()) {
            // Use inner method name instead of outter method name
            String innerMethodName = getInnerName(outterMethodName);
            this.innerMethodMap.put(outterMethodName, innerMethodName);
            this.classWriter.visitField(ACC_PRIVATE, innerMethodName,
                    "Lcom/googlecode/aviator/runtime/type/AviatorFunction;", null, null).visitEnd();
        }
    }

    private String getInnerName(final String varName) {
        return FIELD_PREFIX + this.fieldCounter++;
    }

    private static String getInvokeMethodDesc(final int paramCount) {
        StringBuilder sb = new StringBuilder("(Ljava/util/Map;");
        if (paramCount <= 20) {
            for (int i = 0; i < paramCount; i++) {
                sb.append(OBJECT_DESC);
            }
        } else {
            for (int i = 0; i < 20; i++) {
                sb.append(OBJECT_DESC);
            }
            // variadic params as an array
            sb.append("[Lcom/googlecode/aviator/runtime/type/AviatorObject;");
        }
        sb.append(")Lcom/googlecode/aviator/runtime/type/AviatorObject;");
        return sb.toString();
    }

    @Override
    public void onMethodInvoke(final Token<?> lookhead) {
        visitLineNumber(lookhead);
        @SuppressWarnings("unchecked") final List<FunctionArgument> params = lookhead != null
                ? (List<FunctionArgument>) lookhead.getMeta(Constants.PARAMS_META, Collections.EMPTY_LIST)
                : Collections.EMPTY_LIST;

        int funcId = getNextFuncInvocationId();
        getFuncsArgs().put(funcId, Collections.unmodifiableList(params));
        loadEnv();
        this.mv.visitLdcInsn(FUNC_ARGS_INNER_VAR);
        this.mv.visitLdcInsn(funcId);
        this.mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf",
                "(I)Ljava/lang/Integer;");
        this.mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        this.mv.visitInsn(POP);
        this.pushOperand(2); // __args__ and ref id
        this.popOperand(3); // env, __args and ref id
        this.pushOperand(); // the put result
        this.popOperand(); // pop the put result.

        final MethodMetaData methodMetaData = this.methodMetaDataStack.pop();
        final int parameterCount = methodMetaData.parameterCount;
        if (parameterCount >= 20) {
            if (parameterCount == 20) {
                // pop the list
                this.mv.visitInsn(Opcodes.POP);
                this.popOperand();
            } else {
                // to array
                this.mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "size", "()I");
                this.mv.visitTypeInsn(Opcodes.ANEWARRAY, OBJECT_OWNER);
                int arrayIndex = getLocalIndex();
                this.mv.visitVarInsn(ASTORE, arrayIndex);
                this.mv.visitVarInsn(ALOAD, methodMetaData.variadicListIndex);
                this.mv.visitVarInsn(ALOAD, arrayIndex);
                this.mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "toArray",
                        "([Ljava/lang/Object;)[Ljava/lang/Object;");

                this.mv.visitTypeInsn(CHECKCAST, "[Lcom/googlecode/aviator/runtime/type/AviatorObject;");

                this.popOperand(); // pop list to get size
                this.pushOperand(2); // new array, store and load it, then load the list
                this.popOperand(); // list.toArray
            }
        }
        this.mv.visitMethodInsn(INVOKEINTERFACE, "com/googlecode/aviator/runtime/type/AviatorFunction",
                "call", getInvokeMethodDesc(parameterCount));
        this.mv.visitMethodInsn(INVOKESTATIC, RUNTIME_UTILS, "assertNotNull",
                "(Lcom/googlecode/aviator/runtime/type/AviatorObject;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");

        this.popOperand(); // method object
        this.popOperand(); // env map
        // pop operands
        if (parameterCount <= 20) {
            this.popOperand(parameterCount);
        } else {
            // 20 params + one array
            this.popOperand(21);
        }
        // push result
        this.pushOperand();
    }

    @Override
    public void onMethodParameter(final Token<?> lookhead) {
        visitLineNumber(lookhead);
        MethodMetaData currentMethodMetaData = this.methodMetaDataStack.peek();
        if (currentMethodMetaData.parameterCount >= 20) {
            // Add last param to variadic param list
            assert currentMethodMetaData.variadicListIndex >= 0;
            this.mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z");
            this.mv.visitInsn(Opcodes.POP);
            this.mv.visitVarInsn(ALOAD, currentMethodMetaData.variadicListIndex);
            this.popOperand(2); // pop list and parameter
            this.pushOperand(); // list.add result
            this.popOperand(); // pop last result
            this.pushOperand(); // load list
        }

        currentMethodMetaData.parameterCount++;
        if (currentMethodMetaData.parameterCount == 20) {
            // create variadic params list for further params
            this.mv.visitTypeInsn(NEW, "java/util/ArrayList");
            this.mv.visitInsn(DUP);
            this.mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", CONSTRUCTOR_METHOD_NAME, "()V");
            int listIndex = getLocalIndex();
            this.mv.visitVarInsn(ASTORE, listIndex);
            this.mv.visitVarInsn(ALOAD, listIndex);
            currentMethodMetaData.variadicListIndex = listIndex;
            this.pushOperand(); // new list
        }

        // // add parameter to list
        // this.mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add",
        // "(Ljava/lang/Object;)Z");
        // // pop boolean
        // this.mv.visitInsn(POP);
        // this.mv.visitVarInsn(ALOAD,
        // this.methodMetaDataStack.peek().parameterListIndex);
    }

    private void pushOperand() {
        this.pushOperand(1);
    }

    public static class MethodMetaData {
        public int parameterCount = 0;

        public int variadicListIndex = -1;

        public final Token<?> token;

        public final String methodName;

        public int funcId = -1;

        public MethodMetaData(final Token<?> token, final String methodName) {
            super();
            this.token = token;
            this.methodName = methodName;
        }
    }

    @Override
    public void onArray(final Token<?> lookhead) {
        onConstant(lookhead);
    }

    @Override
    public void onArrayIndexStart(final Token<?> token) {
        loadEnv();
    }

    @Override
    public void onArrayIndexEnd(final Token<?> lookhead) {
        visitLineNumber(lookhead);
        if (!OperationRuntime.hasRuntimeContext(this.compileEnv, OperatorType.INDEX)) {
            this.mv.visitMethodInsn(INVOKEVIRTUAL, OBJECT_OWNER, "getElement",
                    "(Ljava/util/Map;Lcom/googlecode/aviator/runtime/type/AviatorObject;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
        } else {
            loadOpType(OperatorType.INDEX);
            this.mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/aviator/runtime/op/OperationRuntime",
                    "eval",
                    "(Lcom/googlecode/aviator/runtime/type/AviatorObject;Ljava/util/Map;Lcom/googlecode/aviator/runtime/type/AviatorObject;Lcom/googlecode/aviator/lexer/token/OperatorType;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
            this.popOperand();
        }

        this.popOperand(3);
        this.pushOperand();
    }

    public int getLocalIndex() {
        return this.maxLocals++;
    }

    @Override
    public void onLambdaDefineStart(final Token<?> lookhead) {
        if (this.lambdaGenerator == null) {
            Boolean newLexicalScope = lookhead.getMeta(Constants.SCOPE_META, false);
            Boolean inheritEnv = lookhead.getMeta(Constants.INHERIT_ENV_META, false);
            this.lambdaGenerator = new LambdaGenerator(this.instance, this, this.parser, this.classLoader, newLexicalScope, inheritEnv);
            this.lambdaGenerator.setScopeInfo(this.parser.enterScope(newLexicalScope));
        } else {
            throw new CompileExpressionErrorException("Compile lambda error");
        }
    }

    @Override
    public void onLambdaArgument(final Token<?> lookhead, final FunctionParam param) {
        this.lambdaGenerator.addParam(param);
    }

    @Override
    public void onLambdaBodyStart(final Token<?> lookhead) {
        this.parentCodeGenerator = this.parser.getCodeGenerator();
        this.parser.setCodeGenerator(this.lambdaGenerator);
    }

    @Override
    public void onLambdaBodyEnd(final Token<?> lookhead) {
        // this.lambdaGenerator.compileCallMethod();
        LambdaFunctionBootstrap bootstrap = this.lambdaGenerator.getLmabdaBootstrap();
        if (this.lambdaBootstraps == null) {
            // keep in order
            this.lambdaBootstraps = new LinkedHashMap<String, LambdaFunctionBootstrap>();
        }
        this.lambdaBootstraps.put(bootstrap.getName(), bootstrap);
        visitLineNumber(lookhead);
        genNewLambdaCode(bootstrap);
        this.parser.restoreScope(this.lambdaGenerator.getScopeInfo());
        this.lambdaGenerator = null;
        this.parser.setCodeGenerator(this.parentCodeGenerator);
    }

    @Override
    public void genNewLambdaCode(final LambdaFunctionBootstrap bootstrap) {
        this.mv.visitVarInsn(ALOAD, 0);
        loadEnv();
        this.mv.visitLdcInsn(bootstrap.getName());
        this.mv.visitMethodInsn(INVOKEVIRTUAL, this.className, "newLambda",
                "(Lcom/googlecode/aviator/utils/Env;Ljava/lang/String;)Lcom/googlecode/aviator/runtime/function/LambdaFunction;");
        this.pushOperand(2);
        this.popOperand(2);
    }

    @Override
    public void onMethodName(final Token<?> lookhead) {
        String outtterMethodName = "lambda";
        if (lookhead.getType() != TokenType.Delegate) {
            outtterMethodName = lookhead.getLexeme();
            String innerMethodName = this.innerMethodMap.get(outtterMethodName);
            if (innerMethodName != null) {
                loadAviatorFunction(outtterMethodName, innerMethodName);
            } else {
                createAviatorFunctionObject(outtterMethodName);
            }
        } else {
            loadEnv();
            this.mv.visitMethodInsn(INVOKESTATIC, RUNTIME_UTILS, "getFunction",
                    "(Ljava/lang/Object;Ljava/util/Map;)Lcom/googlecode/aviator/runtime/type/AviatorFunction;");
            this.popOperand();
        }
        if (this.instance.getOptionValue(Options.TRACE_EVAL).bool) {
            this.mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/aviator/runtime/function/TraceFunction",
                    "wrapTrace",
                    "(Lcom/googlecode/aviator/runtime/type/AviatorFunction;)Lcom/googlecode/aviator/runtime/type/AviatorFunction;");
        }
        // FIXME it will not work in compile mode.
        if (lookhead.getMeta(Constants.UNPACK_ARGS, false)) {
            this.mv.visitMethodInsn(INVOKESTATIC, RUNTIME_UTILS, "unpackArgsFunction",
                    "(Lcom/googlecode/aviator/runtime/type/AviatorFunction;)Lcom/googlecode/aviator/runtime/type/AviatorFunction;");
        }

        loadEnv();
        this.methodMetaDataStack.push(new MethodMetaData(lookhead, outtterMethodName));
    }

    private void loadAviatorFunction(final String outterMethodName, final String innerMethodName) {
        Map<String, Integer> name2Index = this.labelNameIndexMap.get(this.currentLabel);
        // Is it stored in local?
        if (name2Index != null && name2Index.containsKey(innerMethodName)) {
            int localIndex = name2Index.get(innerMethodName);
            this.mv.visitVarInsn(ALOAD, localIndex);
            this.pushOperand();
        } else {
            this.mv.visitVarInsn(ALOAD, 0);
            this.mv.visitFieldInsn(GETFIELD, this.className, innerMethodName,
                    "Lcom/googlecode/aviator/runtime/type/AviatorFunction;");
            // Method is used more than once,store it to local for reusing
            if (this.methodTokens.get(outterMethodName) > 1) {
                this.mv.visitInsn(DUP);
                int localIndex = getLocalIndex();
                this.mv.visitVarInsn(ASTORE, localIndex);
                if (name2Index == null) {
                    name2Index = new HashMap<String, Integer>();
                    this.labelNameIndexMap.put(this.currentLabel, name2Index);
                }
                name2Index.put(innerMethodName, localIndex);
                this.pushOperand(2);
                this.popOperand();
            } else {
                this.pushOperand();
            }
        }
    }

    private void loadEnv() {
        // load env
        this.pushOperand();
        this.mv.visitVarInsn(ALOAD, 1);
    }

    private void createAviatorFunctionObject(final String methodName) {
        loadEnv();
        this.pushOperand();
        this.mv.visitLdcInsn(methodName);
        this.mv.visitMethodInsn(INVOKESTATIC, RUNTIME_UTILS, "getFunction",
                "(Ljava/util/Map;Ljava/lang/String;)Lcom/googlecode/aviator/runtime/type/AviatorFunction;");
        this.popOperand(2);
        this.pushOperand();
    }

    @Override
    public void onBitAnd(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.BIT_AND, "bitAnd");
    }

    @Override
    public void onBitOr(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.BIT_OR, "bitOr");
    }

    @Override
    public void onBitXor(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.BIT_XOR, "bitXor");
    }

    @Override
    public void onShiftLeft(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.SHIFT_LEFT, "shiftLeft");

    }

    @Override
    public void onShiftRight(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.SHIFT_RIGHT, "shiftRight");

    }

    @Override
    public void onUnsignedShiftRight(final Token<?> lookhead) {
        visitBinOperator(lookhead, OperatorType.U_SHIFT_RIGHT, "unsignedShiftRight");

    }

}
