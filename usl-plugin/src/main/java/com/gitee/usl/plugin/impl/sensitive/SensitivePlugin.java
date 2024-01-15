package com.gitee.usl.plugin.impl.sensitive;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.grammar.type.USLObject;

import java.util.Optional;

/**
 * @author hongda.li
 */
public class SensitivePlugin implements SuccessPlugin {
    private final SensitiveType type;
    private final SensitiveFactory factory;

    public SensitivePlugin(SensitiveType type) {
        this.type = type;
        this.factory = Singleton.get(SensitiveFactory.class);
    }

    @Override
    public void onSuccess(FunctionSession session) {
        Object original;
        if (session.getResult() instanceof USLObject) {
            original = ((USLObject) session.getResult()).getValue(session.getEnv());
        } else {
            original = session.getResult();
        }

        SensitiveContext context = new SensitiveContext(original, type);

        factory.handleFirstOrElse(context, Singleton.get(DefaultSensitizedStrategy.class));

        Optional.ofNullable(context.getResult()).ifPresent(session::setResult);
    }
}
