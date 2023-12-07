package com.gitee.usl.function.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.UslExecuteException;

import java.util.Map;
import java.util.Objects;

/**
 * @author hongda.li
 */
@FunctionGroup
public class TypeFunction {
    @Function("is_null")
    public Boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }

    @Function("is_empty")
    public Boolean isEmpty(Object obj) {
        if (Boolean.TRUE.equals(isNull(obj))) {
            return Boolean.TRUE;
        }

        if (obj instanceof String) {
            return CharSequenceUtil.isEmpty((String) obj);
        }

        if (obj instanceof Iterable) {
            return CollUtil.isEmpty((Iterable<?>) obj);
        }

        if (obj instanceof Map) {
            return CollUtil.isEmpty((Map<?, ?>) obj);
        }

        throw new UslExecuteException(ResultCode.FAILURE, "不支持的类型 - " + obj.getClass().getName());
    }

    @Function("is_email")
    public Boolean isEmail(String str) {
        return Validator.isEmail(str);
    }

    @Function("is_true")
    public Boolean isTrue(Object obj) {
        return Convert.toBool(obj, Boolean.FALSE);
    }

    @Function("is_false")
    public Boolean isFalse(Object obj) {
        return !Boolean.TRUE.equals(isTrue(obj));
    }

    @Function("is_mobile")
    public Boolean isMobile(String str) {
        return Validator.isMobile(str);
    }

    @Function("is_citizenId")
    public Boolean isCitizenId(String str) {
        return Validator.isCitizenId(str);
    }

    @Function("is_birthday")
    public Boolean isBirthday(String str) {
        return Validator.isBirthday(str);
    }

    @Function("is_ipv4")
    public Boolean isIpv4(String str) {
        return Validator.isIpv4(str);
    }

    @Function("is_ipv6")
    public Boolean isIpv6(String str) {
        return Validator.isIpv6(str);
    }

    @Function("is_mac")
    public Boolean isMac(String str) {
        return Validator.isMac(str);
    }

    @Function("is_plateNumber")
    public Boolean isPlateNumber(String str) {
        return Validator.isPlateNumber(str);
    }

    @Function("is_url")
    public Boolean isUrl(String str) {
        return Validator.isUrl(str);
    }

    @Function("is_chinese")
    public Boolean isChinese(String str) {
        return Validator.isChinese(str);
    }

    @Function("is_generalWithChinese")
    public Boolean isGeneralWithChinese(String str) {
        return Validator.isGeneralWithChinese(str);
    }

    @Function("is_uuid")
    public Boolean isUuid(String str) {
        return Validator.isUUID(str);
    }

    @Function("is_hex")
    public Boolean isHex(String str) {
        return Validator.isHex(str);
    }

    @Function("is_creditCode")
    public Boolean isCreditCode(String str) {
        return Validator.isCreditCode(str);
    }

    @Function("is_carVin")
    public Boolean isCarVin(String str) {
        return Validator.isCarVin(str);
    }

    @Function("is_carDrivingLicence")
    public Boolean isCarDrivingLicence(String str) {
        return Validator.isCarDrivingLicence(str);
    }

    @Function("is_chineseName")
    public Boolean isChineseName(String str) {
        return Validator.isChineseName(str);
    }
}
