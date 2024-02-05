package com.gitee.usl.function.base;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Rot;
import cn.hutool.core.util.IdUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;


/**
 * @author jingshu.zeng
 */
@SuppressWarnings("unused")
@FunctionGroup
public class IDFunction {

    @Function("id_uuid")
    public String id_uuid() {
        return IdUtil.randomUUID();//生成的UUID是带-的字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
    }

    @Function("id_uuid_simple")
    public String id_uuid_simple() {
        return IdUtil.simpleUUID();//生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
    }

    @Function("id_snow")
    public long id_snow() {
        return IdUtil.getSnowflake(1, 1).nextId();
    }

    @Function("base64_encode")
    public String base64_encode(String str) {
        return Base64.encode(str);
    }

    @Function("base64_decode")
    public String base64_decode(String encode) {
        return Base64.decodeStr(encode);
    }

    @Function("rot_encode")
    public String rot_encode(String str) {
        return Rot.encode13(str);
    }

    @Function("rot_decode")
    public String rot_decode(String encode13) {
        return Rot.decode13(encode13);
    }

}
