package com.gitee.usl.test;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
class VisitObjectTest {

    @Test
    void test() {
        USLRunner runner = new USLRunner();
        runner.start();
        Result<Object> run = runner.run(new Param()
                .setScript("var.name + var.age")
                .addContext("var", new ObjectTest().setName("hello").setAge(5)));
        Assertions.assertEquals("hello5", run.getData());
    }

    static class ObjectTest {
        private String name;
        private Integer age;

        public Integer getAge() {
            return age;
        }

        public ObjectTest setAge(Integer age) {
            this.age = age;
            return this;
        }

        public String getName() {
            return name;
        }

        public ObjectTest setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", ObjectTest.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .toString();
        }
    }
}
