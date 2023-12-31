package com.googlecode.aviator.code;

import com.gitee.usl.api.annotation.Description;
import com.googlecode.aviator.*;
import com.googlecode.aviator.code.asm.ASMCodeGenerator;
import com.googlecode.aviator.exception.CompileExpressionErrorException;
import com.googlecode.aviator.lexer.SymbolTable;
import com.googlecode.aviator.lexer.token.*;
import com.googlecode.aviator.lexer.token.DelegateToken.DelegateTokenType;
import com.googlecode.aviator.lexer.token.Token.TokenType;
import com.googlecode.aviator.parser.*;
import com.googlecode.aviator.runtime.FunctionParam;
import com.googlecode.aviator.runtime.LambdaFunctionBootstrap;
import com.googlecode.aviator.runtime.op.OperationRuntime;
import com.googlecode.aviator.runtime.type.*;
import com.googlecode.aviator.utils.Constants;
import com.googlecode.aviator.utils.Env;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author hongda.li
 */
@Description("编译器进行优化的字节码生成器")
public class OptimizeCodeGenerator implements CodeGenerator {

    @Description("以运行期优先的字节码生成器")
    private final EvalCodeGenerator evalCodeGenerator;

    private final List<Token<?>> tokenList = new ArrayList<>();

    private LambdaGenerator lambdaGenerator;

    private CodeGenerator parentCodeGenerator;

    private final AviatorEvaluatorInstance instance;

    private Parser parser;

    private Env compileEnv;

    /**
     * Compiled lambda functions.
     */
    private Map<String, LambdaFunctionBootstrap> lambdaBootstraps;

    private final String sourceFile;

    public OptimizeCodeGenerator(final AviatorEvaluatorInstance instance,
                                 final String sourceFile,
                                 final ClassLoader classLoader) {
        this.instance = instance;
        this.sourceFile = sourceFile;
        this.evalCodeGenerator = new ASMCodeGenerator(instance, sourceFile, (AviatorClassLoader) classLoader);
    }

    private Env getCompileEnv() {
        if (this.compileEnv == null) {
            this.compileEnv = new Env();
            this.compileEnv.setInstance(this.instance);
        }
        return this.compileEnv;
    }

    @Override
    public void setParser(final Parser parser) {
        this.parser = parser;
        this.evalCodeGenerator.setParser(parser);
    }

    private Map<Integer, DelegateTokenType> getIndex2DelegateTypeMap(final OperatorType opType) {
        Map<Integer, DelegateTokenType> result = new HashMap<Integer, DelegateTokenType>();
        switch (opType) {
            case AND:
                result.put(2, DelegateTokenType.And_Left);
                break;
            case OR:
                result.put(2, DelegateTokenType.Join_Left);
                break;
            case TERNARY:
                result.put(4, DelegateTokenType.Ternary_Boolean);
                result.put(2, DelegateTokenType.Ternary_Left);
                break;
        }
        return result;
    }


    private int execute() {
        int exeCount = 0;
        final int size = this.tokenList.size();
        for (int i = 0; i < size; i++) {
            Token<?> token = this.tokenList.get(i);
            if (token.getType() == TokenType.Operator) {
                final OperatorToken op = (OperatorToken) token;
                final OperatorType operatorType = op.getOperatorType();
                final int operandCount = operatorType.getArity();
                switch (operatorType) {
                    case FUNC:
                    case INDEX:
                        // Could not optimize function and index call
                        break;
                    default:
                        // If the operator is override, we don't optimize it.
                        if (OperationRuntime.hasRuntimeContext(getCompileEnv(), operatorType)) {
                            break;
                        }
                        Map<Integer, DelegateTokenType> index2DelegateType =
                                getIndex2DelegateTypeMap(operatorType);
                        final int result =
                                executeOperator(index2DelegateType, token, operatorType, i, operandCount);
                        if (result < 0) {
                            compactTokenList();
                            return exeCount;
                        }
                        exeCount += result;
                        break;
                }

            }
        }
        compactTokenList();
        return exeCount;
    }


    private int executeOperator(final Map<Integer, DelegateTokenType> index2DelegateType,
                                final Token<?> operatorToken, final OperatorType operatorType, final int operatorIndex,
                                int operandCount) {
        Token<?> token = null;
        operandCount += index2DelegateType.size();
        // check if literal expression can be executed
        boolean canExecute = true;
        // operand count
        int count = 0;
        // operand start index
        int operandStartIndex = -1;
        for (int j = operatorIndex - 1; j >= 0; j--) {
            token = this.tokenList.get(j);
            if (token == null) {
                // we must compact token list and retry executing
                return -1;
            }
            final TokenType tokenType = token.getType();
            // Check if operand is a literal operand
            if (!isLiteralOperand(token, tokenType, count + 1, index2DelegateType)) {
                canExecute = false;
                break;
            }
            count++;

            if (count == operandCount) {
                operandStartIndex = j;
                break;
            }
        }

        // if we can execute it on compile
        if (canExecute) {
            // arguments
            AviatorObject[] args = new AviatorObject[operandCount];
            int index = 0;
            for (int j = operandStartIndex; j < operatorIndex; j++) {
                token = this.tokenList.get(j);
                if (token.getType() == TokenType.Delegate) {
                    this.tokenList.set(j, null);
                    continue;
                }
                args[index++] = getAviatorObjectFromToken(token);
                // set argument token to null
                this.tokenList.set(j, null);

            }
            AviatorObject result = OperationRuntime.eval(getCompileEnv(), args, operatorType);
            // set result as token to tokenList for next executing
            this.tokenList.set(operatorIndex, getTokenFromOperand(operatorToken, result));
            return 1;
        }
        return 0;
    }


    private boolean isLiteralOperand(final Token<?> token, final TokenType tokenType, final int index,
                                     final Map<Integer, DelegateTokenType> index2DelegateType) {
        switch (tokenType) {
            case Variable:
                return token == Variable.TRUE || token == Variable.FALSE || token == Variable.NIL;
            case Delegate:
                DelegateTokenType targetDelegateTokenType = index2DelegateType.get(index);
                if (targetDelegateTokenType != null) {
                    return targetDelegateTokenType == ((DelegateToken) token).getDelegateTokenType();
                }
                break;
            case Char:
            case Number:
            case Pattern:
                return true;
            case String:
                return !this.instance.isFeatureEnabled(Feature.StringInterpolation);
        }
        return false;
    }


    /**
     * Get token from executing result
     *
     * @param operand
     * @return
     */
    private Token<?> getTokenFromOperand(final Token<?> operatorToken, final AviatorObject operand) {
        Token<?> token = null;
        switch (operand.getAviatorType()) {
            case JavaType:
                if (operand instanceof AviatorRuntimeJavaType) {
                    Object val = operand.getValue(null);
                    if (val == null) {
                        token = Variable.NIL;
                    } else if (val instanceof Number) {
                        token = new NumberToken((Number) val, val.toString(), operatorToken.getLineNo(),
                                operatorToken.getStartIndex());
                    } else if (val instanceof String || val instanceof Character) {
                        String s = val.toString();
                        token = new StringToken(s, operatorToken.getLineNo(), operatorToken.getStartIndex())
                                .withMeta(Constants.INTER_META, s.contains("#"));
                    } else if (val instanceof Pattern) {
                        token = new PatternToken(((Pattern) val).pattern(), operatorToken.getLineNo(),
                                operatorToken.getStartIndex());
                    } else if (val instanceof Boolean) {
                        token = (boolean) val ? Variable.TRUE : Variable.FALSE;
                    } else {
                        throw new CompileExpressionErrorException("Invalid operand:" + operand.desc(null));
                    }
                } else {
                    throw new CompileExpressionErrorException("Invalid operand:" + operand.desc(null));
                }
                break;
            case Boolean:
                token = operand.booleanValue(null) ? Variable.TRUE : Variable.FALSE;
                break;
            case Nil:
                token = Variable.NIL;
                break;
            case BigInt:
            case Decimal:
            case Double:
            case Long:
                final Number value = (Number) operand.getValue(null);
                token = new NumberToken(value, value.toString(), operatorToken.getLineNo(),
                        operatorToken.getStartIndex());
                break;
            case String:
                final String str = (String) operand.getValue(null);
                token = new StringToken(str, operatorToken.getLineNo(), operatorToken.getStartIndex());
                break;
            case Pattern:
                token = new PatternToken(((AviatorPattern) operand).getPattern().pattern(),
                        operatorToken.getLineNo(), operatorToken.getStartIndex());
                break;
        }
        return token;
    }


    private void compactTokenList() {
        Iterator<Token<?>> it = this.tokenList.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                it.remove();
            }
        }
    }


    private AviatorObject getAviatorObjectFromToken(final Token<?> token) {
        AviatorObject result = null;
        switch (token.getType()) {
            case Number:
                // load numbers
                NumberToken numberToken = (NumberToken) token;
                Number num = numberToken.getNumber();
                result = AviatorNumber.valueOf(num);
                break;
            case String:
                // load string
                result =
                        new AviatorString((String) token.getValue(null), true, true, token.getLineNo());
                break;
            case Pattern:
                // load pattern
                result = new AviatorPattern((String) token.getValue(null));
                break;
            case Variable:
                if (token == Variable.TRUE) {
                    result = AviatorBoolean.TRUE;
                } else if (token == Variable.FALSE) {
                    result = AviatorBoolean.FALSE;
                } else if (token == Variable.NIL) {
                    result = AviatorNil.NIL;
                }
                break;
            case Char:
                result = new AviatorString(String.valueOf(token.getValue(null)), true, true,
                        token.getLineNo());
                break;
        }
        return result;
    }


    @Override
    public Expression getResult(final boolean unboxObject) {
        // execute literal expression
        while (execute() > 0) {
            ;
        }

        @Description("常量Token")
        Set<Token<?>> constants = new HashSet<>();

        Map<String, Integer> methods = new HashMap<>();

        Map<String, VariableMeta> variables = new LinkedHashMap<>();

        for (Token<?> token : this.tokenList) {

            @Description("常量标识：字符串/正则/数字")
            boolean isConstant = ExpressionParser.isConstant(token, this.instance);
            if (isConstant) {
                constants.add(token);
            }

            @Description("变量名称")
            String variableName;
            @Description("变量属性")
            VariableMeta variableMeta;

            switch (token.getType()) {
                case Variable:

                    @Description("保留关键字标识")
                    boolean isKeyword = SymbolTable.isReservedKeyword((Variable) token);
                    if (isKeyword) {
                        continue;
                    }

                    variableName = token.getLexeme();
                    variableMeta = variables.get(variableName);
                    if (variableMeta == null) {
                        variables.put(variableName, new VariableMeta(token.getMeta(Constants.TYPE_META),
                                variableName,
                                token.getMeta(Constants.INIT_META, false),
                                token.getStartIndex()));
                    } else {
                        variableMeta.add(token);
                    }

                    break;
                case Delegate:

                    @Description("委托Token")
                    DelegateToken delegateToken = (DelegateToken) token;

                    @Description("委托Token包装的实际Token")
                    Token<?> realToken = delegateToken.getToken();

                    if (delegateToken.getDelegateTokenType() == DelegateTokenType.Method_Name) {
                        if (realToken == null) {
                            continue;
                        }

                        if (realToken.getType() == TokenType.Variable) {
                            String methodName = token.getLexeme();
                            if (!methods.containsKey(methodName)) {
                                methods.put(methodName, 1);
                            } else {
                                methods.put(methodName, methods.get(methodName) + 1);
                            }
                        }
                    } else if (delegateToken.getDelegateTokenType() == DelegateTokenType.Array) {
                        if (realToken.getType() == TokenType.Variable) {
                            variableName = token.getLexeme();
                            variableMeta = variables.get(variableName);
                            if (variableMeta == null) {
                                variables.put(variableName, new VariableMeta(realToken.getMeta(Constants.TYPE_META),
                                        variableName,
                                        realToken.getMeta(Constants.INIT_META, false),
                                        realToken.getStartIndex()));
                            } else {
                                variableMeta.add(realToken);
                            }
                        }
                    }
                    break;
            }
        }

        Expression exp = null;

        // Last token is a literal token,then return a LiteralExpression
        if (this.tokenList.size() <= 1) {
            if (this.tokenList.isEmpty()) {
                exp = new LiteralExpression(this.instance, null, new ArrayList<>(variables.values()));
            } else {
                final Token<?> lastToken = this.tokenList.get(0);
                if (ExpressionParser.isLiteralToken(lastToken)) {
                    exp = new LiteralExpression(this.instance,
                            getAviatorObjectFromToken(lastToken).getValue(getCompileEnv()),
                            new ArrayList<>(variables.values()));
                }
            }
        }

        if (exp == null) {
            // call asm to generate byte codes
            callASM(variables, methods, constants);
            // get result from asm
            exp = this.evalCodeGenerator.getResult(unboxObject);
        }

        if (exp instanceof BaseExpression) {
            ((BaseExpression) exp).setCompileEnv(getCompileEnv());
            ((BaseExpression) exp).setSourceFile(this.sourceFile);
        }

        return exp;
    }

    @Description("调用ASM字节码生成器")
    private void callASM(final Map<String, VariableMeta> variables,
                         final Map<String, Integer> methods,
                         final Set<Token<?>> constants) {
        this.evalCodeGenerator.initConstants(constants);
        this.evalCodeGenerator.initVariables(variables);
        this.evalCodeGenerator.initMethods(methods);
        this.evalCodeGenerator.setLambdaBootstraps(this.lambdaBootstraps);
        this.evalCodeGenerator.start();

        for (Token<?> token : this.tokenList) {
            switch (token.getType()) {
                case Operator:
                    OperatorToken op = (OperatorToken) token;

                    switch (op.getOperatorType()) {
                        case ADD:
                            this.evalCodeGenerator.onAdd(token);
                            break;
                        case SUB:
                            this.evalCodeGenerator.onSub(token);
                            break;
                        case MULT:
                            this.evalCodeGenerator.onMult(token);
                            break;
                        case Exponent:
                            this.evalCodeGenerator.onExponent(token);
                            break;
                        case DIV:
                            this.evalCodeGenerator.onDiv(token);
                            break;
                        case MOD:
                            this.evalCodeGenerator.onMod(token);
                            break;
                        case EQ:
                            this.evalCodeGenerator.onEq(token);
                            break;
                        case NEQ:
                            this.evalCodeGenerator.onNeq(token);
                            break;
                        case LT:
                            this.evalCodeGenerator.onLt(token);
                            break;
                        case LE:
                            this.evalCodeGenerator.onLe(token);
                            break;
                        case GT:
                            this.evalCodeGenerator.onGt(token);
                            break;
                        case GE:
                            this.evalCodeGenerator.onGe(token);
                            break;
                        case NOT:
                            this.evalCodeGenerator.onNot(token);
                            break;
                        case NEG:
                            this.evalCodeGenerator.onNeg(token);
                            break;
                        case AND:
                            this.evalCodeGenerator.onAndRight(token);
                            break;
                        case OR:
                            this.evalCodeGenerator.onJoinRight(token);
                            break;
                        case FUNC:
                            this.evalCodeGenerator.onMethodInvoke(token);
                            break;
                        case INDEX:
                            this.evalCodeGenerator.onArrayIndexEnd(token);
                            break;
                        case MATCH:
                            this.evalCodeGenerator.onMatch(token);
                            break;
                        case TERNARY:
                            this.evalCodeGenerator.onTernaryRight(token);
                            break;
                        case BIT_AND:
                            this.evalCodeGenerator.onBitAnd(token);
                            break;
                        case BIT_OR:
                            this.evalCodeGenerator.onBitOr(token);
                            break;
                        case BIT_XOR:
                            this.evalCodeGenerator.onBitXor(token);
                            break;
                        case BIT_NOT:
                            this.evalCodeGenerator.onBitNot(token);
                            break;
                        case SHIFT_LEFT:
                            this.evalCodeGenerator.onShiftLeft(token);
                            break;
                        case SHIFT_RIGHT:
                            this.evalCodeGenerator.onShiftRight(token);
                            break;
                        case DEFINE:
                            this.evalCodeGenerator.onAssignment(token.withMeta(Constants.DEFINE_META, true));
                            break;
                        case ASSIGNMENT:
                            this.evalCodeGenerator.onAssignment(token);
                            break;
                        case U_SHIFT_RIGHT:
                            this.evalCodeGenerator.onUnsignedShiftRight(token);
                            break;
                    }
                    break;

                case Delegate:
                    DelegateToken delegateToken = (DelegateToken) token;
                    final Token<?> realToken = delegateToken.getToken();
                    switch (delegateToken.getDelegateTokenType()) {
                        case And_Left:
                            this.evalCodeGenerator.onAndLeft(realToken);
                            break;
                        case Join_Left:
                            this.evalCodeGenerator.onJoinLeft(realToken);
                            break;
                        case Array:
                            this.evalCodeGenerator.onArray(realToken);
                            break;
                        case Index_Start:
                            this.evalCodeGenerator.onArrayIndexStart(realToken);
                            break;
                        case Ternary_Boolean:
                            this.evalCodeGenerator.onTernaryBoolean(realToken);
                            break;
                        case Ternary_Left:
                            this.evalCodeGenerator.onTernaryLeft(realToken);
                            break;
                        case Method_Name:
                            this.evalCodeGenerator.onMethodName(realToken);
                            break;
                        case Method_Param:
                            this.evalCodeGenerator.onMethodParameter(realToken);
                            break;
                        case Lambda_New:
                            this.evalCodeGenerator.genNewLambdaCode(delegateToken.getLambdaFunctionBootstrap());
                            break;
                        case Ternay_End:
                            this.evalCodeGenerator.onTernaryEnd(realToken);
                            break;
                    }
                    break;

                default:
                    this.evalCodeGenerator.onConstant(token);
                    break;
            }
        }
    }

    @Override
    public void onAdd(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.ADD));
    }


    @Override
    public void onAndLeft(final Token<?> token) {
        this.tokenList.add(new DelegateToken(token, DelegateTokenType.And_Left));
    }


    @Override
    public void onAndRight(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.AND));
    }


    @Override
    public void onConstant(final Token<?> token) {
        this.tokenList.add(token);
    }


    @Override
    public void onDiv(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.DIV));
    }


    @Override
    public void onArrayIndexStart(final Token<?> token) {
        this.tokenList.add(new DelegateToken(token, DelegateTokenType.Index_Start));
    }

    @Override
    public void onAssignment(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token,
                (token == null || !token.getMeta(Constants.DEFINE_META, false))
                        ? OperatorType.ASSIGNMENT
                        : OperatorType.DEFINE));
    }


    @Override
    public void onArrayIndexEnd(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.INDEX));
    }


    @Override
    public void onArray(final Token<?> token) {
        this.tokenList.add(new DelegateToken(token, DelegateTokenType.Array));
    }


    @Override
    public void onEq(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.EQ));
    }


    @Override
    public void onGe(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.GE));
    }


    @Override
    public void onGt(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.GT));
    }


    @Override
    public void onJoinLeft(final Token<?> token) {
        this.tokenList.add(new DelegateToken(token, DelegateTokenType.Join_Left));
    }


    @Override
    public void onJoinRight(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.OR));
    }


    @Override
    public void onLe(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.LE));
    }


    @Override
    public void onLt(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.LT));
    }


    @Override
    public void onMatch(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.MATCH));
    }


    @Override
    public void onMethodInvoke(final Token<?> token) {
        OperatorToken operatorToken = new OperatorToken(token, OperatorType.FUNC);
        operatorToken.setMetaMap(operatorToken.getMetaMap());
        this.tokenList.add(operatorToken);
    }


    @Override
    public void onMethodName(final Token<?> token) {
        this.tokenList.add(new DelegateToken(token, DelegateTokenType.Method_Name));
    }

    @Override
    public void onMethodParameter(final Token<?> token) {
        this.tokenList.add(new DelegateToken(token, DelegateTokenType.Method_Param));
    }

    @Override
    public void onLambdaDefineStart(final Token<?> token) {
        if (this.lambdaGenerator == null) {
            // TODO cache?
            Boolean newLexicalScope = token.getMeta(Constants.SCOPE_META, false);
            Boolean inheritEnv = token.getMeta(Constants.INHERIT_ENV_META, false);
            this.lambdaGenerator = new LambdaGenerator(this.instance, this, this.parser,
                    this.evalCodeGenerator.getClassLoader(), this.sourceFile, newLexicalScope, inheritEnv);
            this.lambdaGenerator.setScopeInfo(this.parser.enterScope(newLexicalScope));
        } else {
            throw new CompileExpressionErrorException("Compile lambda error");
        }
    }


    @Override
    public void onLambdaArgument(final Token<?> token, final FunctionParam param) {
        this.lambdaGenerator.addParam(param);
    }


    @Override
    public void onLambdaBodyStart(final Token<?> token) {
        this.parentCodeGenerator = this.parser.getCodeGenerator();
        this.parser.setCodeGenerator(this.lambdaGenerator);
    }


    @Override
    public void onLambdaBodyEnd(final Token<?> token) {
        // this.lambdaGenerator.compileCallMethod();
        LambdaFunctionBootstrap bootstrap = this.lambdaGenerator.getLmabdaBootstrap();
        if (this.lambdaBootstraps == null) {
            this.lambdaBootstraps = new HashMap<String, LambdaFunctionBootstrap>();
        }
        this.lambdaBootstraps.put(bootstrap.getName(), bootstrap);
        DelegateToken delegateToken = new DelegateToken(token, DelegateTokenType.Lambda_New);
        delegateToken.setLambdaFunctionBootstrap(bootstrap);
        this.tokenList.add(delegateToken);
        this.parser.restoreScope(this.lambdaGenerator.getScopeInfo());
        this.lambdaGenerator = null;
        this.parser.setCodeGenerator(this.parentCodeGenerator);
    }


    @Override
    public void onMod(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.MOD));
    }


    @Override
    public void onMult(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.MULT));
    }

    @Override
    public void onExponent(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.Exponent));
    }

    @Override
    public void onNeg(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.NEG));
    }


    @Override
    public void onNeq(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.NEQ));

    }


    @Override
    public void onNot(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.NOT));
    }


    @Override
    public void onSub(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.SUB));
    }


    @Override
    public void onTernaryBoolean(final Token<?> token) {
        this.tokenList.add(new DelegateToken(token, DelegateTokenType.Ternary_Boolean));
    }


    @Override
    public void onTernaryLeft(final Token<?> token) {
        this.tokenList.add(new DelegateToken(token, DelegateTokenType.Ternary_Left));
    }


    @Override
    public void onTernaryRight(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.TERNARY));
    }


    @Override
    public void onTernaryEnd(final Token<?> token) {
        this.tokenList.add(new DelegateToken(token, DelegateTokenType.Ternay_End));
    }


    @Override
    public void onBitAnd(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.BIT_AND));
    }


    @Override
    public void onBitNot(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.BIT_NOT));
    }


    @Override
    public void onBitOr(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.BIT_OR));
    }


    @Override
    public void onShiftLeft(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.SHIFT_LEFT));
    }


    @Override
    public void onShiftRight(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.SHIFT_RIGHT));
    }


    @Override
    public void onUnsignedShiftRight(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.U_SHIFT_RIGHT));
    }


    @Override
    public void onBitXor(final Token<?> token) {
        this.tokenList.add(new OperatorToken(token, OperatorType.BIT_XOR));
    }

}
