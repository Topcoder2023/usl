package com.gitee.usl.kernel;

import cn.hutool.core.net.URLEncodeUtil;
import com.google.common.base.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongda.li
 */
class JsEngineTest {

    @Test
    public void tes() throws UnsupportedEncodingException {
        DatabaseClientKey a = new DatabaseClientKey(1L, "a");
        DatabaseClientKey b = new DatabaseClientKey(1L, "a");
        Map<DatabaseClientKey, String> map = new ConcurrentHashMap<>();
        map.put(a, "a");
        String put = map.get(b);
        System.out.println(put);
       // HttpUtil.downloadString()
    }

    public static final class DatabaseClientKey {
        private final Long tenantId;
        private final String databaseCode;

        public DatabaseClientKey(Long tenantId, String databaseCode) {
            this.tenantId = tenantId;
            this.databaseCode = databaseCode;
        }

        public Long getTenantId() {
            return tenantId;
        }

        public String getDatabaseCode() {
            return databaseCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DatabaseClientKey that = (DatabaseClientKey) o;
            return Objects.equal(tenantId, that.tenantId) && Objects.equal(databaseCode, that.databaseCode);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(tenantId, databaseCode);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", DatabaseClientKey.class.getSimpleName() + "[", "]")
                    .add("tenantId=" + tenantId)
                    .add("databaseCode='" + databaseCode + "'")
                    .toString();
        }
    }

    @Test
    void test() throws ScriptException {
        // 获得javascript的脚本引擎
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

        for (ScriptEngineFactory factory : scriptEngineManager.getEngineFactories()) {
            System.out.println(factory.getEngineName());
            System.out.println(factory.getEngineVersion());
        }

        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

        // 进行脚本编译
        String script = "function process(){\n" +
                "var a=10\n" +
                "var b=3\n" +
                "return a*b-c\n" +
                "}\n" +
                "process()";
        CompiledScript compiledScript = ((Compilable) scriptEngine).compile(script);

        // 绑定java的参数
        Bindings bindings = new SimpleBindings();
        bindings.put("c", 5);

        // 执行并打印结果
        Object result = compiledScript.eval(bindings);
        Assertions.assertEquals(25.0, result);
    }
}
