package com.gitee.usl.function.base;

import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;


/**
 * @author jingshu.zeng
 */


@FunctionGroup
public class NumberFunction {
    @Function("abs")
    public double abs(double number) {
        return Math.abs(number);
    }

    @Function("ceil")
    public double ceil(double number) {
        return Math.ceil(number);
    }
    @Function("max")
    public double max(double... numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("至少需要提供一个数值");
        }

        double max = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        return max;
    }

    @Function("floor")
    public double floor(double num) {
        return Math.floor(num);
    }
    @Function("min")
    public double min(double... numbers) {
        if (numbers.length == 0) {
            throw new IllegalArgumentException("至少需要提供一个数值");
        }

        double min = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        return min;
    }

    @Function("sqrt")
    public double sqrt(double number) {
        if (number < 0) {
            throw new IllegalArgumentException("参数必须为非负数");
        }
        return Math.sqrt(number);
    }

    @Function("round")
    public double round(double number, int numDigits) {
        double factor = Math.pow(10, numDigits);
        return Math.round(number * factor) / factor;
    }

}


