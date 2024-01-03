package com.googlecode.aviator.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLCompileException;
import com.gitee.usl.infra.structure.AwaitVariable;
import com.gitee.usl.infra.structure.FunctionVariable;
import com.gitee.usl.infra.structure.VarVariable;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.asm.Script;
import com.googlecode.aviator.Feature;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.code.CodeGenerator;
import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;
import com.googlecode.aviator.lexer.ExpressionLexer;
import com.gitee.usl.grammar.ScriptKeyword;
import com.googlecode.aviator.lexer.token.CharToken;
import com.googlecode.aviator.lexer.token.DelegateToken;
import com.googlecode.aviator.lexer.token.DelegateToken.DelegateTokenType;
import com.googlecode.aviator.lexer.token.NumberToken;
import com.googlecode.aviator.lexer.token.OperatorType;
import com.googlecode.aviator.lexer.token.PatternToken;
import com.googlecode.aviator.lexer.token.StringToken;
import com.googlecode.aviator.lexer.token.Token;
import com.googlecode.aviator.lexer.token.Token.TokenType;
import com.googlecode.aviator.lexer.token.Variable;
import com.googlecode.aviator.runtime.FunctionArgument;
import com.googlecode.aviator.runtime.FunctionParam;
import com.googlecode.aviator.utils.CommonUtils;
import com.googlecode.aviator.utils.Constants;
import lombok.Getter;

import static com.gitee.usl.infra.enums.ResultCode.*;

/**
 * @author hongda.li
 */
@Description("表达式解析器")
public class ExpressionParser implements Parser {

    @Description("CG次数")
    private int getCGTimes;

    @Description("词法分析器")
    private final ExpressionLexer lexer;

    @Getter
    @Description("当前的Token")
    private Token<?> current;

    @Description("已处理的Token")
    private final ArrayDeque<Token<?>> prevTokens = new ArrayDeque<>();

    @Description("字节码生成器")
    private CodeGenerator codeGenerator;

    @Description("嵌套作用域")
    private ScopeInfo scope;

    @Getter
    @Description("已解析的Token数")
    private int parsedTokens;

    @Description("脚本引擎实例")
    private final ScriptEngine instance;

    @Description("已启用的特性")
    private final Set<Feature> featureSet;

    public Token<?> getPrevToken() {
        return this.prevTokens.peek();
    }

    @Override
    public CodeGenerator getCodeGenerator() {
        return this.codeGenerator;
    }

    @Override
    public ScriptKeyword getSymbolTable() {
        return this.lexer.getSymbolTable();
    }

    @Override
    public void setCodeGenerator(final CodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    @Override
    public ScopeInfo enterScope(final boolean inNewScope) {
        ScopeInfo current = this.scope;
        this.scope = new ScopeInfo(inNewScope);
        return current;
    }

    @Override
    public void restoreScope(final ScopeInfo info) {
        this.scope = info;
    }

    public ExpressionParser(final ScriptEngine instance,
                            final ExpressionLexer lexer,
                            final CodeGenerator codeGenerator) {
        this.lexer = lexer;
        this.instance = instance;
        this.current = this.lexer.scan();
        this.scope = new ScopeInfo(false);
        this.featureSet = this.instance.getOptionValue(Options.FEATURE_SET).featureSet;
        Assert.notNull(this.current, () -> new USLCompileException(ResultCode.SCRIPT_EMPTY));

        this.parsedTokens++;
        setCodeGenerator(codeGenerator);
        getCodeGeneratorWithTimes().setParser(this);
    }

    @Override
    public Script parse() {
        StatementType statementType = statements();
        if (this.current != null) {
            if (statementType == StatementType.Ternary) {
                reportSyntaxError("unexpect token '" + currentTokenLexeme()
                        + "', maybe forget to insert ';' to complete last expression ");
            } else {
                reportSyntaxError("unexpect token '" + currentTokenLexeme() + "'");
            }
        }
        return getCodeGeneratorWithTimes().getResult(true);
    }

    /**
     * Call __reducer_return(result)
     */
    public void returnStatement() {
        move(true);
        CodeGenerator cg = getCodeGeneratorWithTimes();
        cg.onTernaryEnd(this.current);
        if (expectChar(';')) {
            // 'return;' => 'return nil;'
            if (this.scope.newLexicalScope) {
                cg.onMethodName(Constants.ReducerReturnFn);
                cg.onConstant(Variable.NIL);
                cg.onMethodParameter(this.current);
                cg.onMethodInvoke(this.current);
            } else {
                cg.onConstant(Variable.NIL);
            }
            move(true);
            return;
        } else {
            if (this.scope.newLexicalScope) {
                cg.onMethodName(Constants.ReducerReturnFn);
                if (!ternary()) {
                    reportSyntaxError("invalid value for return, missing ';'?");
                }
                cg.onMethodParameter(this.current);
                cg.onMethodInvoke(this.current);
            } else {
                if (!ternary()) {
                    reportSyntaxError("invalid value for return, missing ';'?");
                }
            }
        }

        if (!expectChar(';')) {
            reportSyntaxError("missing ';' for return statement");
        }
        move(true);
    }

    public boolean ternary() {
        int gcTimes = this.getCGTimes;

        if (this.current == Variable.NEW) {
            newStatement();
            return true;
        }

        join();
        if (this.current == null || expectChar(':') || expectChar(',')) {
            return gcTimes < this.getCGTimes;
        }

        Token<?> opToken = this.current;

        if (expectChar('?')) {
            move(true);
            CodeGenerator cg = getCodeGeneratorWithTimes();
            cg.onTernaryBoolean(opToken);
            if (!ternary()) {
                reportSyntaxError("invalid token for ternary operator");
            }
            if (expectChar(':')) {
                move(true);
                cg.onTernaryLeft(this.current);
                if (!ternary()) {
                    reportSyntaxError("invalid token for ternary operator");
                }
                cg.onTernaryRight(this.current);
            } else {
                reportSyntaxError("expect ':'");
            }
        }

        return gcTimes < this.getCGTimes;
    }


    public void join() {
        and();
        while (true) {
            Token<?> opToken = this.current;
            if (expectChar('|')) {
                getCodeGeneratorWithTimes().onJoinLeft(opToken);
                move(true);
                if (expectChar('|')) {
                    move(true);
                    and();
                    getCodeGeneratorWithTimes().onJoinRight(opToken);
                } else {
                    reportSyntaxError("expect '|'");
                }
            } else {
                // Process operator alias
                String alias = this.instance.getOperatorAliasToken(OperatorType.OR);
                if (alias != null) {
                    if (opToken != null && opToken.getType() == TokenType.Variable
                            && opToken.getLexeme().equals(alias)) {
                        CodeGenerator cg = getCodeGeneratorWithTimes();
                        cg.onJoinLeft(opToken);
                        move(true);
                        and();
                        cg.onJoinRight(opToken);
                        continue;
                    }
                }

                break;
            }

        }
    }


    private boolean expectChar(final char ch) {
        if (this.current == null) {
            return false;
        }
        return this.current.getType() == TokenType.Char && ((CharToken) this.current).getCh() == ch;
    }


    public void bitOr() {
        xor();
        while (true) {
            Token<?> opToken = this.current;
            if (expectChar('|')) {
                move(true);
                if (expectChar('|')) {
                    back();
                    break;
                }
                xor();
                getCodeGeneratorWithTimes().onBitOr(opToken);
            } else {
                break;
            }
        }
    }


    public void xor() {
        bitAnd();
        while (true) {
            Token<?> opToken = this.current;
            if (expectChar('^')) {
                move(true);
                bitAnd();
                getCodeGeneratorWithTimes().onBitXor(opToken);
            } else {
                break;
            }
        }
    }


    public void bitAnd() {
        equality();
        while (true) {
            Token<?> opToken = this.current;
            if (expectChar('&')) {
                move(true);
                if (expectChar('&')) {
                    back();
                    break;
                }
                equality();
                getCodeGeneratorWithTimes().onBitAnd(opToken);
            } else {
                break;
            }
        }
    }


    public void and() {
        bitOr();
        while (true) {
            Token<?> opToken = this.current;

            if (expectChar('&')) {
                CodeGenerator cg = getCodeGeneratorWithTimes();
                cg.onAndLeft(opToken);
                move(true);
                if (expectChar('&')) {
                    move(true);
                    bitOr();
                    cg.onAndRight(opToken);
                } else {
                    reportSyntaxError("expect '&'");
                }
            } else {
                // Process operator alias
                String alias = this.instance.getOperatorAliasToken(OperatorType.AND);
                if (alias != null) {
                    if (opToken != null && opToken.getType() == TokenType.Variable
                            && opToken.getLexeme().equals(alias)) {
                        CodeGenerator cg = getCodeGeneratorWithTimes();
                        cg.onAndLeft(opToken);
                        move(true);
                        bitOr();
                        cg.onAndRight(opToken);
                        continue;
                    }
                }

                break;
            }
        }
    }

    public void equality() {
        rel();
        while (true) {
            Token<?> opToken = this.current;
            Token<?> prevToken = getPrevToken();
            if (expectChar('=')) {
                move(true);
                if (expectChar('=')) {
                    move(true);

                    // 如果'=='后面还有一个'='，则也兼容JavaScript的语法
                    if (expectChar('=')) {
                        move(true);
                    }

                    rel();
                    getCodeGeneratorWithTimes().onEq(opToken);
                } else if (expectChar('~')) {
                    // It is a regular expression
                    move(true);
                    rel();
                    getCodeGeneratorWithTimes().onMatch(opToken);
                } else {
                    // this.back();
                    // assignment

                    boolean isVar = false;
                    if (prevToken.getType() == TokenType.Variable) {
                        isVar = true;
                    } else if (prevToken.getType() == TokenType.Char
                            && ((CharToken) prevToken).getCh() == ']') {
                        int depth = 1;
                        boolean beginSearch = false;
                        boolean found = false;
                        for (Token<?> t : this.prevTokens) {
                            if (!beginSearch && t == prevToken) {
                                beginSearch = true;
                                continue;
                            }

                            if (beginSearch && t.getType() == TokenType.Char) {
                                CharToken chToken = (CharToken) t;
                                switch (chToken.getCh()) {
                                    case ']':
                                        depth++;
                                        break;
                                    case '[':
                                        depth--;
                                        break;
                                }
                                if (depth == 0) {
                                    found = true;
                                    continue;
                                }

                            }

                            if (found) {
                                if (t.getType() == TokenType.Variable) {
                                    t.withMeta(Constants.TYPE_META, CompileTypes.Array);
                                }
                                break;
                            }
                        }

                    }

                    statement();

                    // try to find var(prevToken) in right statement, it's not initialized if presents.
                    if (isVar) {
                        checkVarIsInit(prevToken);
                    }

                    instance.ensureFeatureEnabled(Feature.Assignment);
                    getCodeGeneratorWithTimes().onAssignment(opToken);
                }
            } else if (expectChar('!')) {
                move(true);
                if (expectChar('=')) {
                    move(true);
                    rel();
                    getCodeGeneratorWithTimes().onNeq(opToken);
                } else {
                    reportSyntaxError("expect '='");
                }
            } else {
                break;
            }
        }
    }


    private void checkVarIsInit(final Token<?> prevToken) {
        boolean isInit = true;
        for (Token<?> t : this.prevTokens) {
            if (t == prevToken) {
                break;
            }
            // It's in right statement, so it's not initialized.
            if (t.getType() == TokenType.Variable && t.getLexeme().equals(prevToken.getLexeme())) {
                isInit = false;
                break;
            }
        }
        prevToken.withMeta(Constants.INIT_META, isInit);
    }


    public void rel() {
        shift();
        while (true) {
            Token<?> opToken = this.current;
            if (expectChar('<')) {
                move(true);
                if (expectChar('=')) {
                    move(true);
                    expr();
                    getCodeGeneratorWithTimes().onLe(opToken);
                } else {
                    expr();
                    getCodeGeneratorWithTimes().onLt(opToken);
                }
            } else if (expectChar('>')) {
                move(true);
                if (expectChar('=')) {
                    move(true);
                    expr();
                    getCodeGeneratorWithTimes().onGe(opToken);
                } else {
                    expr();
                    getCodeGeneratorWithTimes().onGt(opToken);
                }
            } else {
                break;
            }
        }
    }


    public void shift() {
        expr();
        while (true) {
            Token<?> opToken = this.current;
            if (expectChar('<')) {
                move(true);
                if (expectChar('<')) {
                    move(true);
                    expr();
                    getCodeGeneratorWithTimes().onShiftLeft(opToken);
                } else {
                    back();
                    break;
                }
            } else if (expectChar('>')) {
                move(true);
                if (expectChar('>')) {
                    move(true);
                    if (expectChar('>')) {
                        move(true);
                        expr();
                        getCodeGeneratorWithTimes().onUnsignedShiftRight(opToken);
                    } else {
                        expr();
                        getCodeGeneratorWithTimes().onShiftRight(opToken);
                    }

                } else {
                    back();
                    break;
                }
            } else {
                break;
            }
        }
    }


    public void expr() {
        term();
        while (true) {
            Token<?> opToken = this.current;
            if (expectChar('+')) {
                move(true);
                term();
                getCodeGeneratorWithTimes().onAdd(opToken);
            } else if (expectChar('-')) {
                move(true);
                term();
                getCodeGeneratorWithTimes().onSub(opToken);
            } else {
                break;
            }
        }
    }

    public void exponent() {
        factor();
        while (true) {
            Token<?> opToken = this.current;
            if (expectChar('*')) {
                move(true);
                if (expectChar('*')) {
                    move(true);
                    unary();
                    getCodeGeneratorWithTimes().onExponent(opToken);
                } else {
                    back();
                    break;
                }
            } else {
                break;
            }
        }
    }


    public void term() {
        unary();
        while (true) {
            Token<?> opToken = this.current;
            if (expectChar('*')) {
                move(true);
                unary();
                getCodeGeneratorWithTimes().onMult(opToken);
            } else if (expectChar('/')) {
                move(true);
                unary();
                getCodeGeneratorWithTimes().onDiv(opToken);
            } else if (expectChar('%')) {
                move(true);
                unary();
                getCodeGeneratorWithTimes().onMod(opToken);
            } else {
                break;
            }
        }
    }


    public void unary() {
        Token<?> opToken = this.current;
        if (expectChar('!')) {
            move(true);
            // check if it is a seq function call,"!" as variable
            if (expectChar(',') || expectChar(')')) {
                back();
                exponent();
            } else {
                unary();
                getCodeGeneratorWithTimes().onNot(opToken);
            }
        } else if (expectChar('-')) {
            move(true);
            // check if it is a seq function call,"!" as variable
            if (expectChar(',') || expectChar(')')) {
                back();
                exponent();
            } else {
                unary();
                getCodeGeneratorWithTimes().onNeg(opToken);
            }
        } else if (expectChar('~')) {
            move(true);
            // check if it is a seq function call,"~" as variable
            if (expectChar(',') || expectChar(')')) {
                back();
                exponent();
            } else {
                unary();
                getCodeGeneratorWithTimes().onBitNot(opToken);
            }
        } else {
            exponent();
        }
    }

    private int getLookheadStartIndex() {
        // We should calculate the lookhead token's start index, because the token may be reserved by
        // symbol table and it's start index is wrong.
        return this.current != null ? (this.lexer.getCurrentIndex() - getLookheadLexemeLength()) : -1;
    }

    private int getLookheadLexemeLength() {
        int len = this.current.getLexeme().length();
        if (this.current.getType() == TokenType.String) {
            // Must include quote symbols.
            len += 2;
        }
        return len;
    }

    private String getParamExp(final int lastTokenIndex) {
        if (lastTokenIndex >= 0 && getLookheadStartIndex() >= 0) {
            return this.lexer.getScanString().substring(lastTokenIndex, getLookheadStartIndex());
        } else {
            return null;
        }
    }

    public static final CharToken LEFT_PAREN = new CharToken('(', 0, -1);
    public static final CharToken RIGHT_PAREN = new CharToken(')', 0, -1);


    public boolean isOPVariable(final Token<?> token) {
        if (token.getType() != TokenType.Char) {
            return false;
        }
        CharToken charToken = (CharToken) token;

        move(true);
        if (expectChar(',') || expectChar(')')) {
            back();
            String lexeme = String.valueOf(charToken.getCh());
            if (lexeme.equals("-")) {
                lexeme = "-sub";
            }
            return this.instance.existsSystemFunction(lexeme);
        } else {
            back();
            return false;
        }
    }

    public void factor() {
        if (factor0()) {
            methodInvokeOrArrayAccess();
        }
    }


    private boolean factor0() {
        if (this.current == null) {
            reportSyntaxError("illegal token");
        }
        if (this.current == Variable.END) {
            return false;
        }
        if (expectChar('(')) {
            move(true);
            this.scope.enterParen();
            ternary();
            if (expectChar(')')) {
                move(true);
                this.scope.leaveParen();
            }
        } else if (this.current.getType() == TokenType.Number
                || this.current.getType() == TokenType.String
                || this.current.getType() == TokenType.Variable || this.current == Variable.TRUE
                || this.current == Variable.FALSE || isOPVariable(this.current)) {
            if (this.current.getType() == TokenType.Variable) {
                checkVariableName(this.current);
            }
            // binary operation as variable for seq functions
            if (this.current.getType() == TokenType.Char) {
                CharToken charToken = (CharToken) this.current;
                if (!CommonUtils.isBinaryOperator(charToken.getCh())) {
                    reportSyntaxError("unexpect char '" + charToken.getCh() + "'");
                }
                // make it as variable
                this.current = this.lexer.getSymbolTable().reserve(
                        new Variable(charToken.getLexeme(), charToken.getLineNo(), charToken.getStartIndex()));
            }
            move(true);
            // function
            Token<?> prev = getPrevToken();
            if (prev.getType() == TokenType.Variable && expectChar('(')) {
                if (prev == Variable.LAMBDA) {
                    lambda(false);
                } else if (prev == Variable.FN) {
                    lambda(true);
                } else {
                    method(prev);
                }
            } else if (prev.getType() == TokenType.Variable) {
                if (!arrayAccess()) {
                    getCodeGeneratorWithTimes().onConstant(prev);
                }
            } else {
                getCodeGeneratorWithTimes().onConstant(prev);
            }
        } else if (expectChar('/')) {
            pattern();
        } else if (expectChar('}')) {
            return false;
        } else {
            reportSyntaxError("invalid token");
        }
        return true;
    }


    private void lambda(final boolean fn) {
        instance.ensureFeatureEnabled(Feature.Lambda);
        this.scope.enterLambda();
        getCodeGeneratorWithTimes().onLambdaDefineStart(getPrevToken());
        this.scope.enterParen();
        move(true);
        int paramIndex = 0;
        FunctionParam lastParam = null;
        List<FunctionParam> variadicParams = new ArrayList<>(2);
        if (!expectChar(')')) {
            lastParam = lambdaArgument(paramIndex++);
            if (lastParam.isVariadic()) {
                variadicParams.add(lastParam);
            }

            while (expectChar(',')) {
                move(true);
                lastParam = lambdaArgument(paramIndex++);
                if (lastParam.isVariadic()) {
                    variadicParams.add(lastParam);
                }
            }
        }

        // assert only one variadic param and it's the last one.
        if (variadicParams.size() > 1) {
            reportSyntaxError("The variadic parameter must be the last parameter: `"
                    + variadicParams.get(0).getName() + "`");
        }
        if (variadicParams.size() > 0 && variadicParams.get(0) != lastParam) {
            reportSyntaxError("The variadic parameter must be the last parameter: `"
                    + variadicParams.get(0).getName() + "`");
        }

        if (expectChar(')')) {
            this.scope.leaveParen();
            move(true);

            if (fn) {
                if (!expectChar('{')) {
                    reportSyntaxError("expect '{'");
                }
            } else {
                if (!expectChar('-')) {
                    reportSyntaxError("expect '->' for lambda body");
                }
                move(true);
                if (!expectChar('>')) {
                    reportSyntaxError("expect '->' for lambda body");
                }
            }

            move(true);
            getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
            statements();

            if (fn) {
                if (!expectChar('}')) {
                    reportSyntaxError("missing '}' to close function body");
                }
            } else {
                if (this.current != Variable.END) {
                    reportSyntaxError("expect lambda 'end'");
                }
            }

            getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
            this.scope.leaveLambda();
            move(true);
        }
    }

    @Description("获取当前Token的字面值")
    private String currentTokenLexeme() {
        return this.current == null ? CharSequenceUtil.EMPTY : this.current.getLexeme();
    }

    private FunctionParam lambdaArgument(final int index) {
        if (expectChar('&')) {
            move(true);

            if (this.current.getType() != TokenType.Variable) {
                reportSyntaxError("expect argument name, but is: " + currentTokenLexeme());
            }

            return lambdaArgument0(index, true);
        } else if (this.current.getType() == TokenType.Variable) {
            return lambdaArgument0(index, false);
        } else {
            reportSyntaxError("expect argument name, but is: " + currentTokenLexeme());
            return null;
        }
    }

    private FunctionParam lambdaArgument0(final int index, final boolean isVariadic) {
        CommonUtils.checkJavaIdentifier(this.current.getLexeme());
        final FunctionParam param = new FunctionParam(index, this.current.getLexeme(), isVariadic);
        getCodeGeneratorWithTimes().onLambdaArgument(this.current, param);
        move(true);
        return param;
    }


    private boolean arrayAccess() {
        // check if it is a array index access
        boolean hasArray = false;
        while (expectChar('[')) {
            if (!hasArray) {
                getCodeGeneratorWithTimes().onArray(getPrevToken());
                move(true);
                hasArray = true;
            } else {
                move(true);
            }
            getCodeGeneratorWithTimes().onArrayIndexStart(getPrevToken());
            array();
        }
        return hasArray;

    }


    private void array() {
        this.scope.enterBracket();
        if (getPrevToken() == Variable.TRUE || getPrevToken() == Variable.FALSE
                || getPrevToken() == Variable.NIL) {
            reportSyntaxError(getPrevToken().getLexeme() + " could not use [] operator");
        }
        if (!ternary()) {
            reportSyntaxError("missing index for array access");
        }
        if (expectChar(']')) {
            this.scope.leaveBracket();
            move(true);
            getCodeGeneratorWithTimes().onArrayIndexEnd(this.current);
        }
    }

    private void checkVariableName(final Token<?> token) {
        if (token.getType() == TokenType.Delegate) {
            return;
        }
        if (!((Variable) token).isQuote()) {
            for (String name : token.getLexeme().split("\\.")) {
                CommonUtils.checkJavaIdentifier(name);
            }
        }
    }

    private void methodInvokeOrArrayAccess() {
        while (expectChar('[') || expectChar('(')) {
            if (isConstant(getPrevToken(), this.instance)) {
                break;
            }
            if (expectChar('[')) {
                arrayAccess();
            } else if (expectChar('(')) {
                method(anonymousMethodName());
            }
        }
    }

    private void method(final Token<?> methodName) {
        if (expectChar('(')) {
            this.scope.enterParen();
            checkVariableName(methodName);
            checkFunctionName(methodName, false);
            getCodeGeneratorWithTimes().onMethodName(methodName);
            move(true);
            int paramIndex = 0;
            List<FunctionArgument> params = new ArrayList<>();
            boolean unpackArguments = false;
            int lastTokenIndex = getLookheadStartIndex();
            if (!expectChar(')')) {

                boolean isPackArgs = false;
                if (expectChar('*')) {
                    move(true);
                    if (expectChar('*') || expectChar(',')) {
                        // binary operation as argument
                        back();
                    } else {
                        unpackArguments = true;
                        withMetaBegin();
                        isPackArgs = true;
                    }
                }

                ternary();

                if (isPackArgs) {
                    withMetaEnd(Constants.UNPACK_ARGS, true);
                }

                getCodeGeneratorWithTimes().onMethodParameter(this.current);
                params.add(new FunctionArgument(methodName, paramIndex++, getParamExp(lastTokenIndex)));
                while (expectChar(',')) {
                    move(true);
                    isPackArgs = false;
                    lastTokenIndex = getLookheadStartIndex();
                    if (expectChar('*')) {
                        move(true);
                        if (expectChar('*') || expectChar(',')) {
                            // binary operation as argument
                            back();
                        } else {
                            unpackArguments = true;
                            withMetaBegin();
                            isPackArgs = true;
                        }
                    }

                    if (!ternary()) {
                        reportSyntaxError("invalid argument");
                    }

                    if (isPackArgs) {
                        withMetaEnd(Constants.UNPACK_ARGS, true);
                    }

                    getCodeGeneratorWithTimes().onMethodParameter(this.current);
                    params.add(new FunctionArgument(methodName, paramIndex++, getParamExp(lastTokenIndex)));
                }
            }
            if (unpackArguments) {
                methodName.withMeta(Constants.UNPACK_ARGS, true);
            }
            if (expectChar(')')) {
                getCodeGeneratorWithTimes()
                        .onMethodInvoke(currentToken().withMeta(Constants.PARAMS_META, params));
                move(true);
                this.scope.leaveParen();
            }
        }
    }

    private void pattern() {
        // It is a pattern
        int startIndex = this.current.getStartIndex();
        move(true);
        boolean inPattern = true;
        StringBuilder sb = new StringBuilder();
        while (this.current != null) {
            while (!expectChar('/') && this.current != null) {
                sb.append(this.current.getLexeme());
                move(false);
            }
            if (getPrevToken().getType() == TokenType.Char
                    && ((CharToken) getPrevToken()).getLexeme().equals("\\")) {
                sb.append("/");
                move(false);
                continue;
            }
            inPattern = false;
            break;
        }
        if (inPattern) {
            reportSyntaxError("invalid regular pattern:" + sb.toString());
        }
        getCodeGeneratorWithTimes()
                .onConstant(new PatternToken(sb.toString(), this.lexer.getLineNo(), startIndex));
        move(true);
    }


    public void reportSyntaxError(final String message) {
        int index = isValidLookhead() ? this.current.getStartIndex() : this.lexer.getCurrentIndex();

        if (this.current != null) {
            this.lexer.pushback(this.current);
        }

        String msg = "Syntax error: " + message + //
                " at " + index + //
                ", lineNumber: " + this.lexer.getLineNo() + //
                ", token : " + //
                this.current + ",\nwhile parsing expression: `\n" + //
                this.lexer.getScanString() + "^^^\n`";

        ExpressionSyntaxErrorException e = new ExpressionSyntaxErrorException(msg);
        StackTraceElement[] traces = e.getStackTrace();
        List<StackTraceElement> filteredTraces = new ArrayList<>();
        for (StackTraceElement t : traces) {
            if (!this.instance.getOptionValue(Options.TRACE_EVAL).bool
                    && t.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            filteredTraces.add(t);
        }
        e.setStackTrace(filteredTraces.toArray(new StackTraceElement[filteredTraces.size()]));
        throw e;
    }

    private boolean isValidLookhead() {
        return this.current != null && this.current.getStartIndex() > 0;
    }

    public void move(final boolean analyse) {
        if (this.current != null) {
            this.prevTokens.push(this.current);
            this.current = this.lexer.scan(analyse);
            if (this.current != null) {
                this.parsedTokens++;
            }
        } else {
            reportSyntaxError("illegal token");
        }
    }

    @Description("回溯到上一个Token")
    public void back() {
        if (this.current != null) {
            this.parsedTokens--;
        }
        this.lexer.pushback(this.current);
        this.current = getPrevToken();
    }

    enum StatementType {
        Ternary, Return, Empty, Other
    }

    /**
     * Call __reducer_break()
     */
    private void breakStatement() {
        if (!this.scope.newLexicalScope) {
            reportSyntaxError("break only can be used in for-loop");
        }
        move(true);
        getCodeGeneratorWithTimes().onMethodName(Constants.ReducerBreakFn);
        getCodeGeneratorWithTimes().onMethodInvoke(this.current);
        if (!expectChar(';')) {
            reportSyntaxError("missing ';' for break");
        }
        move(true);
    }

    /**
     * Call __reducer_cont(nil)
     */
    private void continueStatement() {
        if (!this.scope.newLexicalScope) {
            reportSyntaxError("continue only can be used in for-loop");
        }
        move(true);
        getCodeGeneratorWithTimes().onMethodName(Constants.ReducerContFn);
        getCodeGeneratorWithTimes().onConstant(Variable.NIL);
        getCodeGeneratorWithTimes().onMethodParameter(this.current);
        getCodeGeneratorWithTimes().onMethodInvoke(this.current);
        if (!expectChar(';')) {
            reportSyntaxError("missing ';' for continue");
        }
        move(true);
    }


    /**
     * <pre>
     *  while(test) {
     *     ...body...
     *  }
     *  ...statements...
     * </pre>
     * <p>
     * ===>
     *
     * <pre>
     *  __reducer_callcc(__reducer_loop, lambda() ->
     *       if(test) {
     *          ...body...
     *       }else {
     *          break;
     *       }
     *  end, lambda()- >
     *       ...statements...
     *  end);
     * </pre>
     */
    private void whileStatement() {
        move(true);

        // prepare to call __reducer_callcc(LOOP, iterator, statements)
        getCodeGeneratorWithTimes().onMethodName(Constants.ReducerFn);
        getCodeGeneratorWithTimes().onConstant(Constants.REDUCER_LOOP);
        getCodeGeneratorWithTimes().onMethodParameter(this.current);

        // create a lambda function wraps while body(iterator)
        boolean newLexicalScope = this.scope.newLexicalScope;
        this.scope.newLexicalScope = true;
        {
            getCodeGeneratorWithTimes().onLambdaDefineStart(
                    getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope));
            getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
            ifStatement(true, false);
            getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
            getCodeGenerator().onMethodParameter(this.current);
        }

        if (expectChar(';')) {
            // the statement is ended.
            getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
        } else {
            // create a lambda function wraps statements after while(statements)
            getCodeGeneratorWithTimes().onLambdaDefineStart(
                    getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope) //
                            .withMeta(Constants.INHERIT_ENV_META, true));
            getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
            if (statements() == StatementType.Empty) {
                getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
            }
            getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
        }
        getCodeGeneratorWithTimes().onMethodParameter(this.current);

        // call __reducer_callcc(LOOP, iterator, statements)
        getCodeGeneratorWithTimes().onMethodInvoke(this.current);
        // restore newLexicalScope
        this.scope.newLexicalScope = newLexicalScope;

    }

    @Description("变量定义语句")
    private void defineStatement() {
        move(true);
        Token<?> token = this.current;
        checkVariableName(token);
        getCodeGenerator().onConstant(token);
        move(true);
        Assert.isTrue(expectChar('='), () -> new USLCompileException(NOT_USE_EQUAL_WHEN_DEFINE));
        move(true);
        Assert.isFalse(statement() == StatementType.Empty, () -> new USLCompileException(NOT_ASSIGN_VALUE_TO_DEFINE));
        checkVarIsInit(token);
        instance.ensureFeatureEnabled(Feature.Assignment);
        getCodeGeneratorWithTimes().onAssignment(currentToken().withMeta(Constants.DEFINE_META, true));
        Assert.isTrue(expectChar(';'), () -> new USLCompileException(NOT_END_FOR_DEFINE_ASSIGN));
        move(true);
    }

    private void fnStatement() {
        move(true);

        if (expectChar('(')) {
            // Anonymous function
            lambda(true);
        } else {

            checkVariableName(this.current);
            checkFunctionName(this.current, true);
            getCodeGeneratorWithTimes().onConstant(this.current.withMeta(Constants.INIT_META, true)
                    .withMeta(Constants.TYPE_META, CompileTypes.Function));
            move(true);
            if (!expectChar('(')) {
                reportSyntaxError("expect '(' after function name");
            }
            lambda(true);
            instance.ensureFeatureEnabled(Feature.Assignment);
            getCodeGeneratorWithTimes()
                    .onAssignment(currentToken().withMeta(Constants.DEFINE_META, true));
        }
    }

    private void checkFunctionName(final Token<?> token, final boolean warnOnExists) {
        String fnName = token.getLexeme();
        if (ScriptKeyword.isReservedKeyword(fnName)) {
            reportSyntaxError("The function name `" + fnName + "` is a reserved keyword");
        }
        if (warnOnExists && this.instance.getSystemFunctionMap().containsKey(fnName)) {
            System.out.println("[Aviator WARN] The function '" + fnName
                    + "' is already exists, but is replaced with new one.");
        }
    }

    private Token<?> currentToken() {
        Token<?> token = this.current;
        if (token == null) {
            token = new CharToken((char) -1, this.lexer.getLineNo(), this.lexer.getCurrentIndex());
        }
        return token;
    }


    private boolean scopeStatement() {
        /**
         * <pre>
         *   {
         *    ...body...
         *   }
         *   ...statements...
         * </pre>
         *
         * =>
         *
         * <pre>
         *  __if_callcc((lambda() -> ...body... end)(),  lambda() ->
         *     ...statements...
         *  end);
         * </pre>
         */

        boolean hasReturn = false;
        boolean newLexicalScope = this.scope.newLexicalScope;
        this.scope.newLexicalScope = true;
        // prepare to call __if_callcc(result, statements)
        getCodeGeneratorWithTimes().onMethodName(Constants.IfReturnFn);

        // Create a lambda to wrap the scope body.
        {
            move(true);
            this.scope.enterBrace();
            getCodeGeneratorWithTimes().onLambdaDefineStart(
                    getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope));
            getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
            hasReturn = statements() == StatementType.Return;
            getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
            getCodeGeneratorWithTimes().onMethodName(anonymousMethodName());
            getCodeGeneratorWithTimes().onMethodInvoke(this.current);

            if (!expectChar('}')) {
                reportSyntaxError("missing '}' to close scope");
            }
            move(true);
            this.scope.leaveBrace();
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
        }

        if (expectChar(';')) {
            // the statement is ended.
            getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
        } else {
            // create a lambda function wraps statements after scope statement (statements)
            getCodeGeneratorWithTimes().onLambdaDefineStart(
                    getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope) //
                            .withMeta(Constants.INHERIT_ENV_META, true));
            getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
            if (statements() == StatementType.Empty) {
                getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
            }
            getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
        }
        getCodeGenerator().onMethodParameter(this.current);

        // call __if_callcc(result, statements)
        getCodeGenerator().onMethodInvoke(this.current);
        this.scope.newLexicalScope = newLexicalScope;

        return hasReturn;
    }

    private void tryStatement() {
        getCodeGeneratorWithTimes().onMethodName(Constants.TRY_VAR);
        move(true);
        if (!expectChar('{')) {
            reportSyntaxError("expect '{' after try");
        }
        move(true);

        boolean newLexicalScope = this.scope.newLexicalScope;
        this.scope.newLexicalScope = true;
        // create a lambda function wraps try body
        {

            getCodeGeneratorWithTimes().onLambdaDefineStart(
                    getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope));
            getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
            statements();
            getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
        }
        if (!expectChar('}')) {
            reportSyntaxError("missing '}' for try body");
        }
        move(true);
        boolean hasCatch = false;
        boolean hasFinally = false;

        while (this.current == Variable.CATCH) {
            if (!hasCatch) {
                // create a handler list.
                getCodeGeneratorWithTimes().onMethodName(Constants.SEQ_LIST_VAR);
                hasCatch = true;
            }

            move(true);
            // create a lambda function wraps catch handlers
            if (!expectChar('(')) {
                reportSyntaxError("expect '(' after catch");
            }
            move(true);
            if (this.current == null || this.current.getType() != TokenType.Variable) {
                reportSyntaxError("invalid exception class name");
            }
            checkVariableName(this.current);
            List<Token<?>> exceptionClasses = new ArrayList<>();
            exceptionClasses.add(this.current);
            move(true);

            Token<?> boundVar = null;
            if (expectChar(')')) {
                // catch(e) to catch all.
                boundVar = exceptionClasses.remove(0);
                exceptionClasses.add(Constants.THROWABLE_VAR);
            } else {
                // catch multi exception
                while (expectChar('|')) {
                    move(true);
                    if (this.current.getType() != TokenType.Variable) {
                        reportSyntaxError("invalid exception class to catch");
                    }
                    checkVariableName(this.current);
                    exceptionClasses.add(this.current);
                    move(true);
                }
                if (this.current == null || this.current.getType() != TokenType.Variable) {
                    reportSyntaxError("invalid bound variable name for exception");
                }
                checkVariableName(this.current);
                boundVar = this.current;
                move(true);
            }

            if (!expectChar(')')) {
                reportSyntaxError("missing ')' for catch caluse");
            }
            move(true);

            if (!expectChar('{')) {
                reportSyntaxError("missing '{' for catch block");
            }
            move(true);
            {
                // create a catch handler
                getCodeGeneratorWithTimes().onMethodName(Constants.CATCH_HANDLER_VAR);
                getCodeGeneratorWithTimes().onLambdaDefineStart(
                        getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope));
                getCodeGeneratorWithTimes().onLambdaArgument(boundVar,
                        new FunctionParam(0, boundVar.getLexeme(), false));
                getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
                statements();
                getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
                getCodeGeneratorWithTimes().onMethodParameter(this.current);
                for (Token<?> exceptionClass : exceptionClasses) {
                    getCodeGeneratorWithTimes().onConstant(exceptionClass);
                    getCodeGeneratorWithTimes().onMethodParameter(this.current);
                }
                getCodeGeneratorWithTimes().onMethodInvoke(this.current);
            }
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
            if (!expectChar('}')) {
                reportSyntaxError("missing '}' for to complete catch block");
            }
            move(true);
        }

        if (hasCatch) {
            // Invoke seq.list to create handler list
            getCodeGeneratorWithTimes().onMethodInvoke(this.current);
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
        } else {
            getCodeGeneratorWithTimes().onConstant(Variable.NIL);
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
        }

        if (this.current == Variable.FINALLY) {
            hasFinally = true;
            move(true);
            if (!expectChar('{')) {
                reportSyntaxError("missing '{' for finally block");
            }
            move(true);
            // create a lambda to
            getCodeGeneratorWithTimes().onLambdaDefineStart(
                    getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope));
            getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
            statements();
            getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
            if (!expectChar('}')) {
                reportSyntaxError("missing '}' for finally block");
            }
            move(true);
        } else {
            getCodeGeneratorWithTimes().onConstant(Variable.NIL);
        }

        if (!hasCatch && !hasFinally) {
            reportSyntaxError("missing catch or finally blocks for catch");
        }

        if (expectChar(';')) {
            // The statement is ended.
            getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
        } else {
            // create a lambda function wraps statements after try..catch
            getCodeGeneratorWithTimes().onLambdaDefineStart(
                    getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope) //
                            .withMeta(Constants.INHERIT_ENV_META, true));
            getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
            if (statements() == StatementType.Empty) {
                getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
            }
            getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
        }

        getCodeGeneratorWithTimes().onMethodParameter(this.current);

        this.scope.newLexicalScope = newLexicalScope;
        getCodeGeneratorWithTimes().onMethodParameter(this.current);
        getCodeGeneratorWithTimes().onMethodInvoke(this.current);
    }

    private void throwStatement() {
        getCodeGeneratorWithTimes().onMethodName(Constants.THROW_VAR);
        move(true);
        statement();
        getCodeGeneratorWithTimes().onMethodParameter(this.current);
        getCodeGeneratorWithTimes().onMethodInvoke(this.current);
        if (!expectChar(';')) {
            reportSyntaxError("missing ';' for throw statement");
        }
    }

    private void newStatement() {
        instance.ensureFeatureEnabled(Feature.NewInstance);
        getCodeGeneratorWithTimes().onMethodName(Constants.NEW_VAR);
        move(true);

        if (this.current == null || this.current.getType() != TokenType.Variable) {
            reportSyntaxError("invalid class name");
        }
        checkVariableName(this.current);
        getCodeGeneratorWithTimes()
                .onConstant(this.current.withMeta(Constants.TYPE_META, CompileTypes.Class));
        getCodeGeneratorWithTimes().onMethodParameter(this.current);
        move(true);

        if (!expectChar('(')) {
            reportSyntaxError("missing '(' after class name");
        }

        this.scope.enterParen();
        move(true);
        if (!expectChar(')')) {
            ternary();
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
            while (expectChar(',')) {
                move(true);
                if (!ternary()) {
                    reportSyntaxError("invalid argument");
                }
                getCodeGeneratorWithTimes().onMethodParameter(this.current);
            }
        }
        if (!expectChar(')')) {
            reportSyntaxError("missing ')' for new statement");
        }
        getCodeGeneratorWithTimes().onMethodInvoke(this.current);
        move(true);
        this.scope.leaveParen();
    }

    private void className() {
        if (this.current.getType() != TokenType.Variable && !expectChar('*')) {
            reportSyntaxError("expect variable name or * to use");
        }
        if (expectChar('*')) {
            wildcard();
        } else {
            checkVariableName(this.current);
            getCodeGenerator().onConstant(this.current);
        }
        move(true);
    }

    private void useStatement() {
        getCodeGeneratorWithTimes().onMethodName(Constants.USE_VAR);
        move(true);
        className();
        getCodeGeneratorWithTimes().onMethodParameter(this.current);

        if (expectChar('*')) {
            // wildcard
            wildcard();
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
            move(true);
        } else if (expectChar('{')) {
            this.scope.enterBrace();
            move(true);
            className();
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
            while (expectChar(',')) {
                move(true);
                className();
                getCodeGeneratorWithTimes().onMethodParameter(this.current);
            }
            if (!expectChar('}')) {
                reportSyntaxError("expect '}' to complete use statement");
            } else {
                move(true);
                this.scope.leaveBrace();
            }
        }

        getCodeGeneratorWithTimes().onMethodInvoke(this.current);
        if (!expectChar(';')) {
            reportSyntaxError("missing ';' for use statement");
        }
    }


    private void wildcard() {
        getCodeGenerator()
                .onConstant(new Variable("*", this.current.getLineNo(), this.current.getStartIndex()));
    }

    private StatementType statement() {
        if (this.current == Variable.IF) {
            instance.ensureFeatureEnabled(Feature.If);
            if (ifStatement(false, false)) {
                return StatementType.Return;
            } else {
                return StatementType.Other;
            }
        } else if (this.current == Variable.FOR) {
            instance.ensureFeatureEnabled(Feature.ForLoop);
            forStatement();
            return StatementType.Other;
        } else if (this.current == Variable.RETURN || this.current == AwaitVariable.getInstance()) {
            instance.ensureFeatureEnabled(Feature.Return);
            returnStatement();
            return StatementType.Return;
        } else if (this.current == Variable.BREAK) {
            breakStatement();
            return StatementType.Return;
        } else if (this.current == Variable.CONTINUE) {
            continueStatement();
            return StatementType.Return;
        } else if (this.current == Variable.LET || this.current == VarVariable.getInstance()) {
            instance.ensureFeatureEnabled(Feature.Let);
            defineStatement();
            return StatementType.Other;
        } else if (this.current == Variable.WHILE) {
            instance.ensureFeatureEnabled(Feature.WhileLoop);
            whileStatement();
            return StatementType.Other;
        } else if (this.current == Variable.FN || this.current == FunctionVariable.getInstance()) {
            instance.ensureFeatureEnabled(Feature.Fn);
            fnStatement();
            return StatementType.Other;
        } else if (this.current == Variable.TRY) {
            instance.ensureFeatureEnabled(Feature.ExceptionHandle);
            tryStatement();
            return StatementType.Other;
        } else if (this.current == Variable.THROW) {
            instance.ensureFeatureEnabled(Feature.ExceptionHandle);
            throwStatement();
            return StatementType.Other;
        } else if (expectChar('{')) {
            instance.ensureFeatureEnabled(Feature.LexicalScope);
            if (scopeStatement()) {
                return StatementType.Return;
            } else {
                return StatementType.Other;
            }
        } else if (this.current == Variable.USE) {
            instance.ensureFeatureEnabled(Feature.Use);
            useStatement();
            return StatementType.Other;
        } else {
            if (ternary()) {
                return StatementType.Ternary;
            } else {
                return StatementType.Empty;
            }
        }
    }

    private void withMetaBegin() {
        getCodeGeneratorWithTimes().onMethodName(Constants.WithMetaFn);
    }

    private void withMetaEnd(final Object key, final Object value) {
        getCodeGeneratorWithTimes().onMethodParameter(this.current);
        getCodeGeneratorWithTimes().onConstant(valueTotoken(key));
        getCodeGeneratorWithTimes().onMethodParameter(this.current);
        getCodeGeneratorWithTimes().onConstant(valueTotoken(value));
        getCodeGeneratorWithTimes().onMethodParameter(this.current);
        getCodeGeneratorWithTimes().onMethodInvoke(this.current);
    }

    @Description("将常量值转为Token类型")
    private Token<?> valueTotoken(final Object value) {
        if (value instanceof Token) {
            return (Token<?>) value;
        }
        if (value == null) {
            return Variable.NIL;
        }
        if (value instanceof String) {
            return new StringToken((String) value, this.lexer.getLineNo(), this.current.getStartIndex());
        }
        if (value instanceof Number) {
            return new NumberToken((Number) value, value.toString(), this.lexer.getLineNo(), this.current.getStartIndex());
        }
        if (value instanceof Boolean) {
            return Boolean.TRUE.equals(value) ? Variable.TRUE : Variable.FALSE;
        }
        throw new USLCompileException(NOT_SUPPORT_VALUE_TYPE, value.getClass().getName());
    }


    @Description("For循环语句")
    private void forStatement() {
        move(true);

        @Description("For语句后面理论上应该接变量，但是这里兼容可以接左括号")
        boolean needCompatible = TokenType.Char.equals(this.current.getType()) && "(".equals(this.current.getLexeme());
        if (needCompatible) {
            move(true);
            @Description("兼容 JavaScript 语法，允许在for循环里定义变量")
            boolean define = VarVariable.getInstance().equals(this.current) || VarVariable.LET.equals(this.current);
            if (define) {
                move(true);
            }
        }

        @Description("For循环里的内嵌参数，至多存在两个")
        List<Token<?>> reducerArgs = new ArrayList<>(2);

        while (true) {
            Assert.isFalse(reducerArgs.size() > 2, () -> new USLCompileException(TOO_MANY_VARIABLE_IN_LOOP));
            reducerArgs.add(this.current);
            checkVariableName(this.current);
            move(true);
            if (expectChar(',')) {
                move(true);
            } else {
                break;
            }
        }
        Assert.isTrue(this.current == Variable.IN, () -> new USLCompileException(NOT_USE_IN_WITH_FOR));
        move(true);
        {
            getCodeGeneratorWithTimes().onMethodName(Constants.ReducerFn);
            Assert.isTrue(ternary(), () -> new USLCompileException(NOT_LOOP_TYPE_WITH_FOR));
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
        }
        if (needCompatible) {
            move(true);
        }
        Assert.isTrue(expectChar('{'), () -> new USLCompileException(NOT_BIG_BRACKET_ON_LEFT));
        move(true);
        this.scope.enterBrace();
        boolean newLexicalScope = this.scope.newLexicalScope;
        this.scope.newLexicalScope = true;

        {
            withMetaBegin();
            getCodeGeneratorWithTimes().onLambdaDefineStart(getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope));

            for (Token<?> reducerArg : reducerArgs) {
                getCodeGeneratorWithTimes().onLambdaArgument(reducerArg, new FunctionParam(0, reducerArg.getLexeme(), false));
            }
            getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
            statements();
            getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
            withMetaEnd(Constants.ARITIES_META, (long) reducerArgs.size());
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
        }

        Assert.isTrue(expectChar('}'), () -> new USLCompileException(NOT_BIG_BRACKET_ON_RIGHT));
        move(true);
        this.scope.leaveBrace();

        if (expectChar(';')) {
            getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
        } else {
            getCodeGeneratorWithTimes().onLambdaDefineStart(getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope).withMeta(Constants.INHERIT_ENV_META, true));
            getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
            if (statements() == StatementType.Empty) {
                getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
            }
            getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
        }
        getCodeGeneratorWithTimes().onMethodParameter(this.current);
        getCodeGeneratorWithTimes().onMethodInvoke(this.current);
        this.scope.newLexicalScope = newLexicalScope;
    }

    private StatementType statements() {
        if (this.current == null) {
            return StatementType.Empty;
        }

        StatementType stmtType = statement();
        ensureDepthState();
        while (expectChar(';') || stmtType == StatementType.Other || stmtType == StatementType.Return) {

            ensureNoStatementAfterReturn(stmtType);

            if (this.current != null && this.current != Variable.END && !expectChar('}')) {
                getCodeGeneratorWithTimes().onTernaryEnd(this.current);
            }

            if (expectChar(';')) {
                move(true);
            }

            if (this.current == null) {
                break;
            }

            StatementType nextStmtType = statement();
            if (nextStmtType == StatementType.Empty) {
                break;
            }
            stmtType = nextStmtType;
            ensureDepthState();
        }
        ensureNoStatementAfterReturn(stmtType);
        // If the last statement is ternary,it must be ended with END TOKEN such as null token, '}',
        // 'end' keyword, or ';'
        // Otherwise report syntax error.
        if (stmtType == StatementType.Ternary) {
            if (current != null && !expectChar(';') && !expectChar('}') && current != Variable.END) {
                this.back();
                reportSyntaxError("unexpect token '" + currentTokenLexeme()
                        + "', maybe forget to insert ';' to complete last expression ");
            }
        }

        return stmtType;
    }

    @Description("确保Return后无其它语句")
    private void ensureNoStatementAfterReturn(final StatementType statementType) {
        if (statementType == StatementType.Return && this.current != null) {
            boolean error = this.current != Variable.END && !expectChar('}');
            Assert.isFalse(error, () -> new USLCompileException(NOT_EXECUTE_CODE));
        }
    }

    /**
     * <pre>
     *  if(test) {
     *     ...if-body...
     *  }else {
     *     ...else-body...
     *  }
     *  ...statements...
     * </pre>
     * <p>
     * ===>
     *
     * <pre>
     *  __if_callcc(test ? (lambda() -> ...if-body... end)() :  (lambda() -> ...else-body... end)(),
     *   lambda()- >
     *       ...statements...
     *  end);
     * </pre>
     */
    private boolean ifStatement(final boolean isWhile, final boolean isElsif) {
        if (!isWhile) {
            move(true);
        }
        boolean ifBodyHasReturn = false;
        boolean elseBodyHasReturn = false;
        boolean newLexicalScope = this.scope.newLexicalScope;
        this.scope.newLexicalScope = true;
        // prepare to call __if_callcc(result, statements)
        getCodeGeneratorWithTimes().onMethodName(Constants.IfReturnFn);

        {
            if (!ternary()) {
                reportSyntaxError("missing test statement for if");
            }

            getCodeGeneratorWithTimes().onTernaryBoolean(this.current);

            if (expectChar('{')) {
                move(true);
                this.scope.enterBrace();
                getCodeGeneratorWithTimes().onLambdaDefineStart(
                        getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope));
                getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
                ifBodyHasReturn = statements() == StatementType.Return;
                getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
                getCodeGeneratorWithTimes().onMethodName(anonymousMethodName());
                getCodeGeneratorWithTimes().onMethodInvoke(this.current);
                getCodeGeneratorWithTimes().onTernaryLeft(this.current);
            } else {
                reportSyntaxError("expect '{' for " + getLoopKeyword(isWhile) + " statement");
            }
            if (!expectChar('}')) {
                reportSyntaxError("missing '}' to close " + getLoopKeyword(isWhile) + " body");
            }
            this.scope.leaveBrace();
            move(true);

            elseBodyHasReturn = elseStatement(isWhile, isElsif, ifBodyHasReturn);
            getCodeGeneratorWithTimes().onMethodParameter(this.current);
        }

        {
            //
            if (isWhile || isElsif) {
                // Load ReducerEmptyVal directly.
                getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
            } else {

                if (expectChar(';')) {
                    // the statement is ended.
                    getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
                } else {
                    // create a lambda function wraps statements after if statement (statements)
                    getCodeGeneratorWithTimes().onLambdaDefineStart(
                            getPrevToken().withMeta(Constants.SCOPE_META, this.scope.newLexicalScope) //
                                    .withMeta(Constants.INHERIT_ENV_META, true));
                    getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
                    if (statements() == StatementType.Empty) {
                        getCodeGenerator().onConstant(Constants.ReducerEmptyVal);
                    } else {
                        if (ifBodyHasReturn && elseBodyHasReturn) {
                            reportSyntaxError("unreachable code");
                        }
                    }
                    getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
                }
            }
            getCodeGenerator().onMethodParameter(this.current);
            // call __if_callcc(result, statements)
            getCodeGenerator().onMethodInvoke(this.current);
            this.scope.newLexicalScope = newLexicalScope;
        }

        return ifBodyHasReturn && elseBodyHasReturn;
    }

    private String getLoopKeyword(final boolean isWhile) {
        return isWhile ? "while" : "if";
    }

    private boolean elseStatement(final boolean isWhile, boolean isElsif,
                                  final boolean ifBodyHasReturn) {
        if (isWhile) {
            // Call __reducer_break(nil)
            final CodeGenerator cg = getCodeGeneratorWithTimes();
            cg.onMethodName(Constants.ReducerBreakFn);
            cg.onConstant(Variable.NIL);
            cg.onMethodParameter(this.current);
            cg.onMethodInvoke(this.current);
            cg.onTernaryRight(this.current);
            return false;
        }

        if (expectChar(';')) {
            return withoutElse();
        }

        boolean hasReturn = false;
        boolean hasElsif = this.current == Variable.ELSIF;
        boolean hasElse = this.current == Variable.ELSE;
        if (this.current != null && (hasElse || hasElsif || ifBodyHasReturn)) {
            if (hasElse) {
                move(true);
                if (expectChar('{')) {
                    this.scope.enterBrace();
                    move(true);
                    hasReturn = elseBody(false);
                    if (expectChar('}')) {
                        this.scope.leaveBrace();
                        move(true);
                    } else {
                        reportSyntaxError("missing '}' to close 'else' body");
                    }
                } else {
                    reportSyntaxError("expect '{' for else statement");
                }
            } else if (hasElsif) {
                hasReturn = ifStatement(false, true);
                getCodeGenerator().onTernaryRight(this.current);
            } else if (!isElsif) {
                hasReturn = elseBody(true);
            } else {
                return withoutElse();
            }
            return hasReturn;
        } else {
            // Missing else statement, always nil.
            return withoutElse();
        }

    }

    private boolean withoutElse() {
        final CodeGenerator cg = getCodeGeneratorWithTimes();
        cg.onConstant(Variable.NIL);
        cg.onTernaryRight(this.current);
        return false;
    }

    private boolean elseBody(final boolean inheritEnv) {
        getCodeGeneratorWithTimes().onLambdaDefineStart(this.current //
                .withMeta(Constants.SCOPE_META, this.scope.newLexicalScope) //
                .withMeta(Constants.INHERIT_ENV_META, inheritEnv) //
        );
        getCodeGeneratorWithTimes().onLambdaBodyStart(this.current);
        boolean hasReturn = statements() == StatementType.Return;
        getCodeGeneratorWithTimes().onLambdaBodyEnd(this.current);
        getCodeGeneratorWithTimes().onMethodName(anonymousMethodName());
        getCodeGeneratorWithTimes().onMethodInvoke(this.current);
        getCodeGeneratorWithTimes().onTernaryRight(this.current);
        return hasReturn;
    }

    private DelegateToken anonymousMethodName() {
        return new DelegateToken(this.current, DelegateTokenType.Method_Name);
    }


    private void ensureDepthState() {
        DepthState state = this.scope.depthState.peekLast();
        if (state != null) {
            back();
            switch (state) {
                case Parent:
                    if (this.scope.parenDepth > 0) {
                        reportSyntaxError("insert ')' to complete statement");
                    }
                    break;
                case Bracket:
                    if (this.scope.bracketDepth > 0) {
                        reportSyntaxError("insert ']' to complete statement");
                    }
                    break;
                case Lambda:
                    if (this.scope.lambdaDepth > 0) {
                        reportSyntaxError("insert 'end' to complete lambda statement");
                    }
                    break;
                case Brace:
                    if (this.scope.braceDepth > 0) {
                        reportSyntaxError("insert '}' to complete statement");
                    }
                    break;
            }
        }
    }

    public static boolean isConstant(final Token<?> token, final ScriptEngine instance) {
        switch (token.getType()) {
            case Number:
            case Pattern:
            case String:
                return true;
            default:
                return false;
        }
    }

    public static boolean isLiteralToken(final Token<?> token) {
        switch (token.getType()) {
            case Variable:
                return token == Variable.TRUE || token == Variable.FALSE || token == Variable.NIL;
            case Char:
            case Number:
            case Pattern:
            case String:
                return true;
            default:
                return false;
        }
    }

    private CodeGenerator getCodeGeneratorWithTimes() {
        this.getCGTimes++;
        return this.codeGenerator;
    }

}
