package com.question.admin.service.common;

public interface ISyncCacheService {

	Boolean getLock(String lockName, int expireTime);

	Boolean releaseLock(String lockName);
}
