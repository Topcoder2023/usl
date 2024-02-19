package com.gitee.usl.function.domain;

import java.util.Date;

/**
 * @author hongda.li
 */
public record LockInfo(Long id, String lockName, Date createdTime, Date expiredTime) {
}
