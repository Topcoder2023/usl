package com.gitee.usl.function.math;

import cn.hutool.core.util.RandomUtil;
import com.gitee.usl.api.annotation.Func;

import java.math.RoundingMode;

/**
 * 随机数函数
 *
 * @author hongda.li
 */
@Func
public class RandomFunction {

    @Func("random.int")
    public int randomInt(int start, int end) {
        return RandomUtil.randomInt(start, end, true, false);
    }

    @Func("random.long")
    public long randomLong(long start, long end) {
        return RandomUtil.randomLong(start, end, true, false);
    }

    @Func("random.double")
    public double randomDouble(double start, double end, int scale, int mode) {
        RoundingMode roundingMode = RoundingMode.valueOf(mode);
        return RandomUtil.randomDouble(start, end, scale, roundingMode);
    }

    @Func("random.float")
    public float randomFloat(float start, float end) {
        return RandomUtil.randomFloat(start, end);
    }

    @Func("random.string")
    public String randomFloat(String base, int length) {
        return RandomUtil.randomString(base, length);
    }

    @Func("random.chinese")
    public String randomChinese() {
        return String.valueOf(RandomUtil.randomChinese());
    }
}
