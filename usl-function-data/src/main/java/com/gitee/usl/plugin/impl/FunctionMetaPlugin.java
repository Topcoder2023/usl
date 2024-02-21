package com.gitee.usl.plugin.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import com.gitee.usl.api.plugin.FinallyPlugin;
import com.gitee.usl.domain.FunctionMeta;
import com.gitee.usl.infra.ConnectHelper;
import com.gitee.usl.infra.DatabaseConstant;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.exception.USLException;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.kernel.engine.FunctionSession;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author hongda.li
 */
@Slf4j
public class FunctionMetaPlugin implements FinallyPlugin {

    @Override
    public void onFinally(FunctionSession session) {
        try {
            FunctionDefinition definition = session.getDefinition();
            Entity entity = FunctionMeta.selectByName(definition.getRunner().getName(), definition.getName());
            if (entity == null) {
                return;
            }

            Long functionId = entity.getLong(FunctionMeta.ID);
            String params = ArrayUtil.isEmpty(session.getInvocation().args())
                    ? null
                    : JSONUtil.toJsonStr(session.getInvocation().args());
            String status = session.getException() == null
                    ? StringConstant.SUCCESS
                    : StringConstant.FAILURE;
            String result = session.getResult() == null
                    ? null
                    : JSONUtil.toJsonStr(session.getResult());
            String errorMessage = session.getException() == null
                    ? null
                    : Optional.ofNullable(ExceptionUtil.getRootCause(session.getException()))
                    .map(Throwable::getMessage)
                    .orElse(session.getException().getMessage());
            String invokeTime = DateUtil.now();

            Entity insert = Entity.create(DatabaseConstant.TABLE_INVOKE_NAME)
                    .set("FUNCTION_ID", functionId)
                    .set("INVOKE_PARAM", params)
                    .set("INVOKE_STATUS", status)
                    .set("INVOKE_RESULT", result)
                    .set("ERROR_MESSAGE", errorMessage)
                    .set("INVOKE_TIME", invokeTime);

            ConnectHelper.connectSystem().proxy().insert(insert);
        } catch (Exception e) {
            log.error("新增函数调用记录异常", e);
            throw new USLException(e);
        }
    }
}
