/**
 * Copyright (C) 2010 dennis zhuang (killme2008@gmail.com)
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 **/
package com.googlecode.aviator.code;

import com.gitee.usl.api.annotation.Description;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.lexer.token.Token;
import com.googlecode.aviator.parser.Parser;
import com.googlecode.aviator.runtime.FunctionParam;

/**
 * @author hongda.li
 */
@Description("字节码生成器接口")
public interface CodeGenerator {

    Expression getResult(boolean unboxObject);

    void onAssignment(Token<?> token);

    void setParser(Parser parser);

    void onShiftRight(Token<?> token);

    void onShiftLeft(Token<?> token);


    void onUnsignedShiftRight(Token<?> token);


    void onBitOr(Token<?> token);


    void onBitAnd(Token<?> token);


    void onBitXor(Token<?> token);


    void onBitNot(Token<?> token);


    void onAdd(Token<?> token);


    void onSub(Token<?> token);


    void onMult(Token<?> token);

    void onExponent(Token<?> loohead);


    void onDiv(Token<?> token);


    void onAndLeft(Token<?> token);


    void onAndRight(Token<?> token);

    @Description("三元表达式的布尔表达式部分")
    void onTernaryBoolean(Token<?> token);

    void onTernaryLeft(Token<?> token);


    void onTernaryRight(Token<?> token);

    void onTernaryEnd(Token<?> token);


    void onJoinLeft(Token<?> token);


    void onJoinRight(Token<?> token);


    void onEq(Token<?> token);


    void onMatch(Token<?> token);


    void onNeq(Token<?> token);


    void onLt(Token<?> token);


    void onLe(Token<?> token);


    void onGt(Token<?> token);


    void onGe(Token<?> token);


    void onMod(Token<?> token);


    void onNot(Token<?> token);


    void onNeg(Token<?> token);

    void onConstant(Token<?> token);

    void onMethodName(Token<?> token);

    void onMethodParameter(Token<?> token);

    void onMethodInvoke(Token<?> token);

    void onLambdaDefineStart(Token<?> token);

    void onLambdaArgument(Token<?> token, FunctionParam param);

    void onLambdaBodyStart(Token<?> token);

    void onLambdaBodyEnd(Token<?> token);

    void onArray(Token<?> token);

    void onArrayIndexStart(Token<?> token);

    void onArrayIndexEnd(Token<?> token);
}
