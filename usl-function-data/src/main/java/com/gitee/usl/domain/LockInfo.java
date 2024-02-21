package com.gitee.usl.domain;

import java.util.Date;

/**
 * @author hongda.li
 */
public record LockInfo(Long id, String lockName, Date createdTime, Date expiredTime) {
}
