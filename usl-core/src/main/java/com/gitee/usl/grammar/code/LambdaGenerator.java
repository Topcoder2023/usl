package com.gitee.usl.grammar.code;
/**
 * lamdba function generator
 *
 * @author dennis
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.script.Script;
import com.gitee.usl.grammar.lexer.token.Token;
import com.gitee.usl.grammar.ScriptClassLoader;
import com.gitee.usl.grammar.parser.Parser;
import com.gitee.usl.grammar.parser.ScopeInfo;
import com.gitee.usl.grammar.runtime.FunctionParam;
import com.gitee.usl.grammar.runtime.LambdaFunctionBootstrap;

/**
 * Lambda function generator
 *
 * @author dennis
 *
 */
public class LambdaGenerator implements CodeGenerator {
    // private final ClassWriter classWriter;
    private final List<FunctionParam> params;
    private final CodeGenerator codeGenerator;
    private final CodeGenerator parentCodeGenerator;
    // private final AviatorClassLoader classLoader;
    // private final AviatorEvaluatorInstance instance;
    private final String className;
    private static final AtomicLong LAMBDA_COUNTER = new AtomicLong();
    // private MethodVisitor mv;
    private ScopeInfo scopeInfo;
    private final boolean newLexicalScope;
    private final boolean inheritEnv;

    public LambdaGenerator(final ScriptEngine instance,
                           final CodeGenerator parentCodeGenerator, final Parser parser,
                           final ScriptClassLoader classLoader, final boolean newLexicalScope,
                           final boolean inheritEnv) {
        this.params = new ArrayList<>();
        // this.instance = instance;
        this.parentCodeGenerator = parentCodeGenerator;
        this.codeGenerator = instance.codeGenerator(classLoader);
        this.codeGenerator.setParser(parser);
        // this.classLoader = classLoader;
        this.newLexicalScope = newLexicalScope;
        this.inheritEnv = inheritEnv;
        // Generate lambda class name
        this.className =
                "AviatorScript_" + System.currentTimeMillis() + "_" + LAMBDA_COUNTER.getAndIncrement();
        // Auto compute frames
        // this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        // visitClass();
        // makeConstructor();
        // makeGetName();
    }


    public ScopeInfo getScopeInfo() {
        return this.scopeInfo;
    }


    public void setScopeInfo(final ScopeInfo scopeInfo) {
        this.scopeInfo = scopeInfo;
    }


    @Override
    public void setParser(final Parser parser) {
        this.codeGenerator.setParser(parser);
    }


    // /**
    // * Make a default constructor
    // */
    // private void makeConstructor() {
    // {
    // this.mv = this.classWriter.visitMethod(ACC_PUBLIC, "<init>",
    // "(Ljava/util/List;Lcom/googlecode/aviator/Expression;Lcom/googlecode/aviator/utils/Env;)V",
    // null, null);
    // this.mv.visitCode();
    // this.mv.visitVarInsn(ALOAD, 0);
    // this.mv.visitVarInsn(ALOAD, 1);
    // this.mv.visitVarInsn(ALOAD, 2);
    // this.mv.visitVarInsn(ALOAD, 3);
    // this.mv.visitMethodInsn(INVOKESPECIAL,
    // "com/googlecode/aviator/runtime/function/LambdaFunction", "<init>",
    // "(Ljava/util/List;Lcom/googlecode/aviator/Expression;Lcom/googlecode/aviator/utils/Env;)V");
    //
    // this.mv.visitInsn(RETURN);
    // this.mv.visitMaxs(4, 1);
    // this.mv.visitEnd();
    // }
    // }
    //

    /**
     * Make a getName method
     */
    // private void makeGetName() {
    // {
    // this.mv = this.classWriter.visitMethod(ACC_PUBLIC + +ACC_FINAL, "getName",
    // "()Ljava/lang/String;", "()Ljava/lang/String;", null);
    // this.mv.visitCode();
    // this.mv.visitLdcInsn(this.className);
    // this.mv.visitInsn(ARETURN);
    // this.mv.visitMaxs(1, 1);
    // this.mv.visitEnd();
    // }
    // }

    /**
     * Compile a call method to invoke lambda compiled body expression.
     */
    // public void compileCallMethod() {
    // int argsNumber = this.params.size();
    // int arrayIndex = 2 + argsNumber;
    // if (argsNumber < 20) {
    // StringBuilder argsDescSb = new StringBuilder();
    // for (int i = 0; i < argsNumber; i++) {
    // argsDescSb.append("Lcom/googlecode/aviator/runtime/type/AviatorObject;");
    // }
    // String argsDec = argsDescSb.toString();
    //
    // this.mv = this.classWriter.visitMethod(ACC_PUBLIC + +ACC_FINAL, "call",
    // "(Ljava/util/Map;" + argsDec + ")Lcom/googlecode/aviator/runtime/type/AviatorObject;",
    // "(Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;" + argsDec
    // + ")Lcom/googlecode/aviator/runtime/type/AviatorObject;",
    // null);
    // this.mv.visitCode();
    //
    // // load expression field
    // this.mv.visitIntInsn(ALOAD, 0);
    // this.mv.visitFieldInsn(GETFIELD, this.className, "expression",
    // "Lcom/googlecode/aviator/BaseExpression;");
    // // this pointer
    // this.mv.visitIntInsn(ALOAD, 0);
    // // load env
    // this.mv.visitIntInsn(ALOAD, 1);
    // // new array
    // this.mv.visitLdcInsn(argsNumber);
    // this.mv.visitTypeInsn(Opcodes.ANEWARRAY, "com/googlecode/aviator/runtime/type/AviatorObject");
    // this.mv.visitVarInsn(ASTORE, arrayIndex);
    // // load other arguments
    // for (int i = 0; i < argsNumber; i++) {
    // this.mv.visitVarInsn(ALOAD, arrayIndex);
    // this.mv.visitLdcInsn(i);
    // this.mv.visitVarInsn(ALOAD, i + 2);
    // this.mv.visitInsn(AASTORE);
    // }
    // this.mv.visitVarInsn(ALOAD, arrayIndex);
    // this.mv.visitMethodInsn(INVOKEVIRTUAL,
    // "com/googlecode/aviator/runtime/function/LambdaFunction", "newEnv",
    // "(Ljava/util/Map;[Lcom/googlecode/aviator/runtime/type/AviatorObject;)Ljava/util/Map;");
    // // execute body expression
    // this.mv.visitMethodInsn(INVOKEVIRTUAL, "com/googlecode/aviator/BaseExpression",
    // "executeDirectly", "(Ljava/util/Map;)Ljava/lang/Object;");
    // // get the result
    // this.mv.visitMethodInsn(INVOKESTATIC,
    // "com/googlecode/aviator/runtime/type/AviatorRuntimeJavaType", "valueOf",
    // "(Ljava/lang/Object;)Lcom/googlecode/aviator/runtime/type/AviatorObject;");
    // this.mv.visitInsn(ARETURN);
    // this.mv.visitMaxs(5, 1);
    // this.mv.visitEnd();
    // } else {
    // throw new CompileExpressionErrorException("Lambda function arguments number at most 20.");
    // }
    // }
    //
    // private void visitClass() {
    // this.classWriter.visit(this.instance.getBytecodeVersion(), ACC_PUBLIC + ACC_SUPER,
    // this.className, null, "com/googlecode/aviator/runtime/function/LambdaFunction", null);
    // }
    //
    //
    //
    // private void endVisitClass() {
    // this.classWriter.visitEnd();
    // }
    public LambdaFunctionBootstrap getLmabdaBootstrap() {
        Script expression = getResult(!this.newLexicalScope);
        // endVisitClass();
        // byte[] bytes = this.classWriter.toByteArray();
        // try {

        // Class<?> defineClass =
        // ClassDefiner.defineClass(this.className, LambdaFunction.class, bytes, this.classLoader);
        //
        // Constructor<?> constructor =
        // defineClass.getConstructor(List.class, Expression.class, Env.class);
        // MethodHandle methodHandle = MethodHandles.lookup().unreflectConstructor(constructor);
        return new LambdaFunctionBootstrap(this.className, expression, this.params, this.inheritEnv);
        // } catch (Exception e) {
        // throw new CompileExpressionErrorException("define lambda class error", e);
        // }
    }


    public void addParam(final FunctionParam name) {
        this.params.add(name);
    }


    @Override
    public void onShiftRight(final Token<?> headToken) {
        this.codeGenerator.onShiftRight(headToken);
    }


    @Override
    public void onShiftLeft(final Token<?> headToken) {
        this.codeGenerator.onShiftLeft(headToken);
    }


    @Override
    public void onUnsignedShiftRight(final Token<?> headToken) {
        this.codeGenerator.onUnsignedShiftRight(headToken);
    }


    @Override
    public void onAssignment(final Token<?> headToken) {
        this.codeGenerator.onAssignment(headToken);
    }


    @Override
    public void onBitOr(final Token<?> headToken) {
        this.codeGenerator.onBitOr(headToken);
    }


    @Override
    public void onBitAnd(final Token<?> headToken) {
        this.codeGenerator.onBitAnd(headToken);
    }


    @Override
    public void onBitXor(final Token<?> headToken) {
        this.codeGenerator.onBitXor(headToken);
    }


    @Override
    public void onBitNot(final Token<?> headToken) {
        this.codeGenerator.onBitNot(headToken);
    }


    @Override
    public void onAdd(final Token<?> headToken) {
        this.codeGenerator.onAdd(headToken);
    }

    @Override
    public void onSub(final Token<?> headToken) {
        this.codeGenerator.onSub(headToken);
    }


    @Override
    public void onMult(final Token<?> headToken) {
        this.codeGenerator.onMult(headToken);
    }


    @Override
    public void onExponent(final Token<?> token) {
        this.codeGenerator.onExponent(token);
    }


    @Override
    public void onDiv(final Token<?> headToken) {
        this.codeGenerator.onDiv(headToken);
    }


    @Override
    public void onAndLeft(final Token<?> headToken) {
        this.codeGenerator.onAndLeft(headToken);
    }


    @Override
    public void onAndRight(final Token<?> headToken) {
        this.codeGenerator.onAndRight(headToken);
    }


    @Override
    public void onTernaryBoolean(final Token<?> headToken) {
        this.codeGenerator.onTernaryBoolean(headToken);
    }


    @Override
    public void onTernaryLeft(final Token<?> headToken) {
        this.codeGenerator.onTernaryLeft(headToken);
    }


    @Override
    public void onTernaryRight(final Token<?> headToken) {
        this.codeGenerator.onTernaryRight(headToken);
    }


    @Override
    public void onTernaryEnd(final Token<?> headToken) {
        this.codeGenerator.onTernaryEnd(headToken);
    }


    @Override
    public void onJoinLeft(final Token<?> headToken) {
        this.codeGenerator.onJoinLeft(headToken);
    }


    @Override
    public void onJoinRight(final Token<?> headToken) {
        this.codeGenerator.onJoinRight(headToken);
    }


    @Override
    public void onEq(final Token<?> headToken) {
        this.codeGenerator.onEq(headToken);
    }


    @Override
    public void onMatch(final Token<?> headToken) {
        this.codeGenerator.onMatch(headToken);
    }


    @Override
    public void onNeq(final Token<?> headToken) {
        this.codeGenerator.onNeq(headToken);
    }


    @Override
    public void onLt(final Token<?> headToken) {
        this.codeGenerator.onLt(headToken);
    }


    @Override
    public void onLe(final Token<?> headToken) {
        this.codeGenerator.onLe(headToken);
    }


    @Override
    public void onGt(final Token<?> headToken) {
        this.codeGenerator.onGt(headToken);
    }


    @Override
    public void onGe(final Token<?> headToken) {
        this.codeGenerator.onGe(headToken);
    }


    @Override
    public void onMod(final Token<?> headToken) {
        this.codeGenerator.onMod(headToken);
    }


    @Override
    public void onNot(final Token<?> headToken) {
        this.codeGenerator.onNot(headToken);
    }


    @Override
    public void onNeg(final Token<?> headToken) {
        this.codeGenerator.onNeg(headToken);
    }


    @Override
    public Script getResult(final boolean unboxObject) {
        return this.codeGenerator.getResult(unboxObject);
    }

    @Override
    public void genNewLambdaCode(LambdaFunctionBootstrap bootstrap) {

    }


    @Override
    public void onConstant(final Token<?> headToken) {
        this.codeGenerator.onConstant(headToken);
    }

    @Override
    public void onMethodName(final Token<?> headToken) {
        this.codeGenerator.onMethodName(headToken);
    }


    @Override
    public void onMethodParameter(final Token<?> headToken) {
        this.codeGenerator.onMethodParameter(headToken);
    }

    @Override
    public void onMethodInvoke(final Token<?> headToken) {
        this.codeGenerator.onMethodInvoke(headToken);
    }


    @Override
    public void onLambdaDefineStart(final Token<?> headToken) {
        this.codeGenerator.onLambdaDefineStart(headToken);
    }


    @Override
    public void onLambdaArgument(final Token<?> headToken, final FunctionParam param) {
        this.codeGenerator.onLambdaArgument(headToken, param);
    }

    @Override
    public void onLambdaBodyStart(final Token<?> headToken) {
        this.codeGenerator.onLambdaBodyStart(headToken);
    }

    @Override
    public void onLambdaBodyEnd(final Token<?> headToken) {
        // should call parent generator
        this.parentCodeGenerator.onLambdaBodyEnd(headToken);
    }

    @Override
    public void onArray(final Token<?> headToken) {
        this.codeGenerator.onArray(headToken);
    }

    @Override
    public void onArrayIndexStart(final Token<?> token) {
        this.codeGenerator.onArrayIndexStart(token);
    }

    @Override
    public void onArrayIndexEnd(final Token<?> headToken) {
        this.codeGenerator.onArrayIndexEnd(headToken);
    }

}
