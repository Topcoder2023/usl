package com.manda.usl.kernel;

import com.google.auto.service.AutoService;
import com.manda.usl.kernel.annotation.Func;
import com.manda.usl.kernel.common.FuncScanner;

/**
 * @author hongda.li
 */
@AutoService(FuncScanner.class)
public class TestScanner implements FuncScanner {
    @Override
    public void doScan(Scanner scanner) {
        scanner.scan(new NumberFunctions());
    }

    @Func
    static class NumberFunctions {

        @Func("test_sum")
        double sum(int a, double b, float c) {
            return a + b + c;
        }
    }
}
