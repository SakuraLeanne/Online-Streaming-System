package com.dhgx.learning.common.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于本地开发/测试的内存对象存储实现。
 */
public class InMemoryObjectStorageClient implements ObjectStorageClient {

    private final Map<String, byte[]> storage = new ConcurrentHashMap<String, byte[]>();

    @Override
    public String upload(String objectKey, byte[] bytes) {
        storage.put(objectKey, bytes);
        return "memory://" + objectKey;
    }
}
