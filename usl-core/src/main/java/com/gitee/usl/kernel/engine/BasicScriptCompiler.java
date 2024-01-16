package com.gitee.usl.kernel.engine;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.ScriptCompiler;
import com.gitee.usl.api.VariableDefinable;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.lexer.GrammarLexer;
import com.gitee.usl.grammar.parser.Generator;
import com.gitee.usl.grammar.parser.ExpressionParser;
import com.gitee.usl.grammar.parser.IRGenerator;
import com.gitee.usl.grammar.script.BS;
import com.gitee.usl.grammar.script.ES;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLCompileException;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.configure.EngineConfig;
import com.gitee.usl.kernel.configure.CompilerConfig;
import com.gitee.usl.kernel.domain.Param;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author hongda.li
 */
@Slf4j
@Setter
@Getter
@Order(Integer.MAX_VALUE - 10)
public class BasicScriptCompiler implements Initializer, ScriptCompiler {

    private ScriptEngine scriptEngine;

    private Cache<String, Param> cache;

    private VariableDefinable definable;

    private FunctionHolder functionHolder;

    @Override
    public void doInit(Configuration configuration) {
        EngineConfig engineConfig = configuration.getEngineConfig();
        CompilerConfig storageConfig = configuration.getCompilerConfig();

        this.definable = engineConfig.getVarInitializer();
        this.functionHolder = engineConfig.getFunctionHolder();
        this.scriptEngine = engineConfig.getEngineInitializer().getInstance();
        this.cache = CacheUtil.newLRUCache(storageConfig.getSize(), storageConfig.getDuration().toMillis());

        storageConfig.setScriptCompiler(this);
    }

    @Override
    public void compile(Param param) {
        String key = DigestUtil.sha256Hex(param.getScript());
        log.debug("新增缓存 - [{}]", key);
        Param value = cache.get(key, (Func0<Param>) () -> param.self().setCompiled(this.compile(param.getScript())));
        if (!param.isCached()) {
            cache.remove(key);
            log.debug("剔除缓存 - [{}]", key);
        }

        BS compiled = (BS) value.getCompiled();
        param.setCompiled(compiled);
        if (compiled == null || compiled instanceof ES || definable == null) {
            return;
        }

        compiled.getSymbolTable()
                .getTable()
                .entrySet()
                .stream()
                .filter(entry -> functionHolder.search(entry.getKey()) == null)
                .forEach(entry -> Optional.ofNullable(definable.define(entry.getValue()))
                        .ifPresent(result -> {
                            param.addContext(entry.getKey(), result);
                            log.debug("初始化变量 - [{} : {}]", entry.getKey(), result);
                        }));
    }

    protected BS compile(String script) {
        if (CharSequenceUtil.isBlank(script)) {
            return ES.empty().setException(new USLCompileException(ResultCode.SCRIPT_EMPTY));
        } else {
            log.debug("开始编译脚本\n{}", script);
            Generator generator = new IRGenerator(scriptEngine);
            GrammarLexer grammarLexer = new GrammarLexer(scriptEngine, script);
            return (BS) new ExpressionParser(scriptEngine, grammarLexer, generator).parse();
        }
    }

}
