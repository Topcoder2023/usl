package com.gitee.usl.kernel.engine.factory;

import com.gitee.usl.api.UslInitializer;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.engine.UslFunctionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * USL 函数工厂管理者
 * 负责将所有的 USL 函数工厂初始化并加载到脚本引擎配置中
 *
 * @author hongda.li
 */
public class UslFunctionFactoryManager implements UslInitializer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        EngineConfiguration configuration = uslConfiguration.getEngineConfiguration();
        List<UslFunctionFactory> factoryList = SpiServiceUtil.services(UslFunctionFactory.class);

        factoryList.forEach(factory -> logger.info("Register USL Factory - [{}]", factory.getClass().getName()));

        configuration.setFactoryList(factoryList);
    }
}
