package com.gitee.usl.api.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.gitee.usl.api.ScriptCompiler;
import com.gitee.usl.api.VariableDefinable;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.lexer.GrammarLexer;
import com.gitee.usl.grammar.parser.Generator;
import com.gitee.usl.grammar.parser.ScriptParser;
import com.gitee.usl.grammar.parser.IRGenerator;
import com.gitee.usl.grammar.script.BS;
import com.gitee.usl.grammar.script.ES;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLCompileException;
import com.gitee.usl.infra.structure.FunctionHolder;
import com.gitee.usl.domain.Param;
import com.gitee.usl.kernel.engine.USLConfiguration;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 默认的脚本编译器
 *
 * @author hongda.li
 */
@Slf4j
@Setter
@Getter
public class DefaultScriptCompiler implements ScriptCompiler {

    private final ScriptEngine engine;

    private final Cache<String, Param> cache;

    private final VariableDefinable definable;

    private final FunctionHolder functionHolder;

    public DefaultScriptCompiler(USLConfiguration configuration) {
        this.engine = configuration.getEngine();
        this.definable = configuration.getDefinable();
        this.functionHolder = configuration.getFunctionHolder();
        this.cache = Caffeine.newBuilder()
                .softValues()
                .maximumSize(configuration.getSize())
                .expireAfterAccess(configuration.getExpired())
                .evictionListener((RemovalListener<String, Param>) (key, param, removalCause) -> log.debug("剔除缓存 - [{}]", key))
                .build();
    }

    @Override
    public void compile(Param param) {
        String key = DigestUtil.sha256Hex(param.getScript());

        Param value = cache.get(key, k -> param.self().setCompiled(this.compile(key, param.getScript())));

        if (!param.isCached()) {
            cache.invalidate(key);
            log.debug("剔除缓存 - [{}]", key);
        }

        BS compiled = (BS) value.getCompiled();
        param.setCompiled(compiled);

        if (compiled == null || compiled instanceof ES || this.definable == null) {
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

    protected BS compile(String key, String script) {
        log.debug("新增缓存 - [{}]", key);
        if (CharSequenceUtil.isBlank(script)) {
            return ES.empty().setException(new USLCompileException(ResultCode.SCRIPT_EMPTY));
        } else {
            log.debug("开始编译脚本\n{}", script);
            Generator generator = new IRGenerator(engine);
            GrammarLexer grammarLexer = new GrammarLexer(engine, script);
            return (BS) new ScriptParser(engine, grammarLexer, generator).parse();
        }
    }

}
