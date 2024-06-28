package com.manda.usl.base;

import com.google.auto.service.AutoService;
import com.manda.usl.kernel.common.FuncScanner;

/**
 * @author hongda.li
 */
@AutoService(FuncScanner.class)
public class CommonFuncScanner implements FuncScanner {

    @Override
    public void doScan(Scanner scanner) {
        scanner.scan(this.getClass());
    }
}
