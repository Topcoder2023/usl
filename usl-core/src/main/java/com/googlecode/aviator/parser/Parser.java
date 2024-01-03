package com.googlecode.aviator.parser;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.asm.Script;
import com.googlecode.aviator.code.CodeGenerator;
import com.gitee.usl.grammar.ScriptKeyword;

/**
 * @author hongda.li
 */
public interface Parser {

    @Description("解析表达式并编译")
    Script parse();

    ScriptKeyword getSymbolTable();

    CodeGenerator getCodeGenerator();

    void setCodeGenerator(CodeGenerator codeGenerator);

    ScopeInfo enterScope(boolean inForLoop);

    void restoreScope(ScopeInfo info);

}
