package com.gitee.usl.plugin.impl.sensitive;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.impl.sensitive.DefaultSensitizedStrategy;
import com.gitee.usl.plugin.impl.sensitive.SensitiveContext;
import com.gitee.usl.plugin.impl.sensitive.SensitiveFactory;
import com.gitee.usl.plugin.impl.sensitive.SensitiveType;
import com.googlecode.aviator.runtime.type.AviatorObject;

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
        if (session.getResult() instanceof AviatorObject) {
            original = ((AviatorObject) session.getResult()).getValue(session.getEnv());
        } else {
            original = session.getResult();
        }

        SensitiveContext context = new SensitiveContext(original, type);

        factory.handleFirstOrElse(context, Singleton.get(DefaultSensitizedStrategy.class));

        Optional.ofNullable(context.getResult()).ifPresent(session::setResult);
    }
}
