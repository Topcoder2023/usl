package com.gitee.usl.function.math;

import cn.hutool.core.util.RandomUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.math.RoundingMode;

/**
 * 随机数函数
 *
 * @author hongda.li
 */
@FunctionGroup
public class RandomFunction {

    @Function("random_int")
    public int randomInt(int start, int end) {
        return RandomUtil.randomInt(start, end, true, false);
    }

    @Function("random_long")
    public long randomLong(long start, long end) {
        return RandomUtil.randomLong(start, end, true, false);
    }

    @Function("random_double")
    public double randomDouble(double start, double end, int scale, int mode) {
        RoundingMode roundingMode = RoundingMode.valueOf(mode);
        return RandomUtil.randomDouble(start, end, scale, roundingMode);
    }

    @Function("random_float")
    public float randomFloat(float start, float end) {
        return RandomUtil.randomFloat(start, end);
    }

    @Function("random_string")
    public String randomFloat(String base, int length) {
        return RandomUtil.randomString(base, length);
    }

    @Function("random_chinese")
    public String randomChinese() {
        return String.valueOf(RandomUtil.randomChinese());
    }
}
