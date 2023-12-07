package com.gitee.usl.function.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.UslExecuteException;

import java.util.Map;
import java.util.Objects;

/**
 * @author hongda.li
 */
@Func
public class TypeFunction {
    @Func("is.null")
    public Boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }

    @Func("is.empty")
    public Boolean isEmpty(Object obj) {
        if (Boolean.TRUE.equals(this.isNull(obj))) {
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

    @Func("is.email")
    public Boolean isEmail(String str) {
        return Validator.isEmail(str);
    }

    @Func("is.true")
    public Boolean isTrue(Object obj) {
        return Convert.toBool(obj, Boolean.FALSE);
    }

    @Func("is.false")
    public Boolean isFalse(Object obj) {
        return !Boolean.TRUE.equals(this.isTrue(obj));
    }

    @Func("is.mobile")
    public Boolean isMobile(String str) {
        return Validator.isMobile(str);
    }

    @Func("is.citizenId")
    public Boolean isCitizenId(String str) {
        return Validator.isCitizenId(str);
    }

    @Func("is.birthday")
    public Boolean isBirthday(String str) {
        return Validator.isBirthday(str);
    }

    @Func("is.ipv4")
    public Boolean isIpv4(String str) {
        return Validator.isIpv4(str);
    }

    @Func("is.ipv6")
    public Boolean isIpv6(String str) {
        return Validator.isIpv6(str);
    }

    @Func("is.mac")
    public Boolean isMac(String str) {
        return Validator.isMac(str);
    }

    @Func("is.plateNumber")
    public Boolean isPlateNumber(String str) {
        return Validator.isPlateNumber(str);
    }

    @Func("is.url")
    public Boolean isUrl(String str) {
        return Validator.isUrl(str);
    }

    @Func("is.chinese")
    public Boolean isChinese(String str) {
        return Validator.isChinese(str);
    }

    @Func("is.generalWithChinese")
    public Boolean isGeneralWithChinese(String str) {
        return Validator.isGeneralWithChinese(str);
    }

    @Func("is.uuid")
    public Boolean isUuid(String str) {
        return Validator.isUUID(str);
    }

    @Func("is.hex")
    public Boolean isHex(String str) {
        return Validator.isHex(str);
    }

    @Func("is.creditCode")
    public Boolean isCreditCode(String str) {
        return Validator.isCreditCode(str);
    }

    @Func("is.carVin")
    public Boolean isCarVin(String str) {
        return Validator.isCarVin(str);
    }

    @Func("is.carDrivingLicence")
    public Boolean isCarDrivingLicence(String str) {
        return Validator.isCarDrivingLicence(str);
    }

    @Func("is.chineseName")
    public Boolean isChineseName(String str) {
        return Validator.isChineseName(str);
    }
}
