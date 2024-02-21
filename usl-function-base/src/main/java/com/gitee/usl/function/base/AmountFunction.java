package com.gitee.usl.function.base;


import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.text.DecimalFormat;


/**
 * @author jingshu.zeng
 */


@FunctionGroup
public class AmountFunction {
    @Function("amount_to_capital_chi")
    public static String amount_to_capital_chi(double number) {
        // 大写数字
        final String[] NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        // 整数部分的单位
        final String[] IUNIT = {"元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};
        // 小数部分的单位
        final String[] DUNIT = {"角", "分", "厘"};

        String str = String.valueOf(number);
        // 判断输入的金额字符串是否符合要求
        if (str == null || str.trim().isEmpty() || !str.matches("(-)?[\\d]*(.)?[\\d]*")) {
            return "抱歉，请输入数字！";
        }

        // 判断金额是否为零
        if (number == 0) {
            return "零元";
        }

        // 判断是否为负数
        boolean flag = false;
        if (number < 0) {
            flag = true;
            str = str.replaceAll("-", "");
        }

        // 去掉金额数字中的逗号","
        str = str.replaceAll(",", "");
        String integerStr;//整数部分数字
        String decimalStr;//小数部分数字

        // 初始化：分离整数部分和小数部分
        if (str.indexOf(".") > 0) {
            integerStr = str.substring(0, str.indexOf("."));
            decimalStr = str.substring(str.indexOf(".") + 1);
        } else if (str.indexOf(".") == 0) {
            integerStr = "";
            decimalStr = str.substring(1);
        } else {
            integerStr = str;
            decimalStr = "";
        }

        // beyond超出计算能力，直接返回
        if (integerStr.length() > IUNIT.length) {
            return "超出计算能力！";
        }

        // 整数部分数字
        int[] integers = toIntArray(integerStr);
        // 判断整数部分是否存在输入012的情况
        if (integers.length > 1 && integers[0] == 0) {
            return "抱歉，输入数字不符合要求！";
        }
        // 设置万单位
        boolean isWan = isWan5(integerStr);
        // 小数部分数字
        int[] decimals = toIntArray(decimalStr);
        // 返回最终的大写金额
        String result = getChineseInteger(integers, isWan, NUMBERS, IUNIT) + getChineseDecimal(decimals, NUMBERS, DUNIT);
        if (flag) {
            // 如果是负数，加上"负"
            return "负" + result;
        } else {
            return result;
        }
    }

    private static int[] toIntArray(String str) {
        int[] array = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            array[i] = str.charAt(i) - '0';
        }
        return array;
    }

    private static boolean isWan5(String integerStr) {
        int length = integerStr.length();
        if (length > 4) {
            String subInteger = "";
            if (length > 8) {
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }
            return Integer.parseInt(subInteger) > 0;
        } else {
            return false;
        }
    }

    private static String getChineseInteger(int[] integers, boolean isWan, String[] NUMBERS, String[] IUNIT) {
        StringBuffer sb = new StringBuffer();
        int length = integers.length;
        for (int i = 0; i < length; i++) {
            // 0出现在关键位置：1234(万)5678(亿)9012(万)3456(元)
            // 特殊情况：10(十元 一十元 十元)
            String key = "";
            if (integers[i] == 0) {
                if ((length - i) == 13) {// 万(亿)(必填)
                    key = IUNIT[4];
                } else if ((length - i) == 9) {// 亿(必填)
                    key = IUNIT[8];
                } else if ((length - i) == 5 && isWan) {// 万(不必填)
                    key = IUNIT[4];
                } else if ((length - i) == 1) {// 元(必填)
                    key = IUNIT[0];
                }
                if ((length - i) > 1 && integers[i + 1] != 0) {
                    key += NUMBERS[0];
                }
            }
            sb.append(integers[i] == 0 ? key : (NUMBERS[integers[i]] + IUNIT[length - i - 1]));
        }
        return sb.toString();
    }

    private static String getChineseDecimal(int[] decimals, String[] NUMBERS, String[] DUNIT) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < decimals.length; i++) {
            if (i == 3) {
                break;
            }
            sb.append(decimals[i] == 0 ? "" : (NUMBERS[decimals[i]] + DUNIT[i]));
        }
        return sb.toString();
    }


    @Function("amount_to_capital_eng")
    public static String amount_to_capital_eng(double number) {
        // 数字金额转换为英文大写金额
        String result = convertToWords(number);

        // 返回转换结果
        return "SAY US DOLLARS " + result;
    }

    // 数字金额转换为英文大写金额的具体实现
    private static String convertToWords(double number) {
        // 英文数字
        final String[] NUMBERS = {"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE"};
        // 英文数字的十位数
        final String[] TENS = {"", "", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"};
        // 英文数字的十到十九
        final String[] TEENS = {"TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"};
        // 金额单位
        final String[] UNITS = {"", "THOUSAND", "MILLION", "BILLION", "TRILLION"};

        // 格式化金额，保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");
        String amountString = df.format(number);

        // 拆分整数部分和小数部分
        String[] parts = amountString.split("\\.");
        long integerPart = Long.parseLong(parts[0]);
        int decimalPart = Integer.parseInt(parts[1]);

        // 转换整数部分
        String integerWords = convertIntegerPart(integerPart, NUMBERS, TENS, TEENS, UNITS);

        // 转换小数部分
        String decimalWords = convertDecimalPart(decimalPart, NUMBERS, TENS);

        // 构建最终结果
        StringBuilder result = new StringBuilder();
        result.append(integerWords);
        if (decimalPart > 0) {
            result.append(" AND CENTS ").append(decimalWords);
        }
        result.append(" ONLY");

        return result.toString();
    }

    // 转换整数部分为英文大写
    private static String convertIntegerPart(long number, String[] NUMBERS, String[] TENS, String[] TEENS, String[] UNITS) {
        if (number == 0) {
            return "ZERO";
        }

        String words = "";
        int unitIndex = 0;

        do {
            // 获取当前三位数
            long part = number % 1000;
            if (part != 0) {
                // 转换当前三位数为英文大写
                String partWords = convertThreeDigits(part, NUMBERS, TENS, TEENS);
                // 拼接单位
                words = partWords + " " + UNITS[unitIndex] + " " + words;
            }
            // 处理下一个三位数
            number /= 1000;
            unitIndex++;
        } while (number > 0);

        return words.trim();
    }

    // 转换小数部分为英文大写
    private static String convertDecimalPart(int number, String[] NUMBERS, String[] TENS) {
        if (number == 0) {
            return "ZERO";
        }

        // 十位数
        int tens = number / 10;
        // 个位数
        int ones = number % 10;

        if (tens == 0) {
            return NUMBERS[ones];
        } else if (tens == 1) {
            return TENS[ones];
        } else {
            return TENS[tens] + "-" + NUMBERS[ones];
        }
    }

    // 转换三位数为英文大写
    private static String convertThreeDigits(long number, String[] NUMBERS, String[] TENS, String[] TEENS) {
        int hundreds = (int) (number / 100);
        int remainder = (int) (number % 100);

        StringBuilder words = new StringBuilder();
        if (hundreds > 0) {
            words.append(NUMBERS[hundreds]).append(" HUNDRED ");
        }
        if (remainder > 0) {
            if (hundreds > 0) {
                words.append("AND ");
            }
            if (remainder < 10) {
                words.append(NUMBERS[remainder]);
            } else if (remainder < 20) {
                words.append(TEENS[remainder - 10]);
            } else {
                int tens = remainder / 10;
                int ones = remainder % 10;
                words.append(TENS[tens]);
                if (ones > 0) {
                    words.append("-").append(NUMBERS[ones]);
                }
            }
        }
        return words.toString().trim();
    }
}



