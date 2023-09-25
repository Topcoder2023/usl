package com.gitee.usl.infra.utils;

import com.gitee.usl.infra.constant.NumberConstant;

/**
 * @author hongda.li
 */
public class NumberUtil {
    private NumberUtil() {
    }

    public static IntWrapper ofIntWrapper() {
        return new IntWrapper(NumberConstant.ZERO);
    }

    public static final class IntWrapper {
        private int value;

        private IntWrapper(int value) {
            this.value = value;
        }

        public int get() {
            return this.value;
        }

        public int set(int value) {
            this.value = value;
            return this.value;
        }

        public void increment() {
            this.value++;
        }

        public int getAndIncrement() {
            return this.value++;
        }

        public int incrementAndGet() {
            return ++this.value;
        }
    }
}
