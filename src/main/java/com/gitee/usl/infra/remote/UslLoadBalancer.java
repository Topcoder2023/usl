package com.gitee.usl.infra.remote;

import java.util.List;

/**
 * @author hongda.li
 */
public interface UslLoadBalancer {

    /**
     * 从给定的远程服务列表中选择其中一个服务
     * 选择的算法由实现类决定
     *
     * @param remoteServiceList 远程服务列表
     * @return 被选择的服务
     */
    UslRemoteService choose(List<UslRemoteService> remoteServiceList);
}
