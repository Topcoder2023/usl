package com.gitee.usl.infra.utils;

import com.gitee.usl.infra.constant.NumberConstant;

/**
 * @author hongda.li
 */
public class ObjectWrapper {
    private ObjectWrapper() {
    }

    public static IntWrapper ofIntWrapper() {
        return new IntWrapper(NumberConstant.ZERO);
    }

    public static BoolWrapper ofBoolWrapper() {
        return new BoolWrapper(false);
    }

    public static final class BoolWrapper {
        private boolean value;

        public BoolWrapper(boolean value) {
            this.value = value;
        }

        public boolean get() {
            return this.value;
        }

        public boolean set(boolean value) {
            this.value = value;
            return this.value;
        }
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
