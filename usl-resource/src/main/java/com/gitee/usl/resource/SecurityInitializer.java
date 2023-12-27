package com.gitee.usl.resource;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.resource.filter.SecurityFilter;
import com.google.auto.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author hongda.li
 */
@AutoService(Initializer.class)
public class SecurityInitializer implements Initializer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doInit(Configuration configuration) {
        String key = "key";

        AES aes;
        if (key == null) {
            aes = new AES(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()));
        } else {
            aes = new AES(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes(StandardCharsets.UTF_8)));
        }

        String byteArray = ArrayUtil.toString(aes.getSecretKey().getEncoded());
        logger.info("AES密钥构建成功 - {}", byteArray);

        SecurityFilter.setAes(aes);
    }
}
