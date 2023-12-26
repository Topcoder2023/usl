package com.gitee.usl.infra.utils;

import com.gitee.usl.api.annotation.Description;
import com.google.common.hash.Hashing;
import com.googlecode.aviator.BaseExpression;
import com.googlecode.aviator.Expression;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author hongda.li
 */
public class ScriptCompileHelper {

    @Description("空的表达式占位对象")
    private static final Expression EMPTY_PLACE_HOLDER = new BaseExpression(null, null, null) {
        private static final long serialVersionUID = -3628284719346231510L;

        @Override
        public Object executeDirectly(Map<String, Object> env) {
            return null;
        }
    };

    private ScriptCompileHelper() {
    }

    public static Expression empty() {
        return EMPTY_PLACE_HOLDER;
    }

    public static boolean isEmpty(Expression expression) {
        return EMPTY_PLACE_HOLDER.equals(expression) || expression == null;
    }

    @Description("生成脚本内容对应的唯一缓存键")
    @SuppressWarnings("UnstableApiUsage")
    public static String generateKey(String content) {
        return Hashing.sha512()
                .newHasher()
                .putString(content, StandardCharsets.UTF_8)
                .hash()
                .toString();
    }
}
