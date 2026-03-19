package com.dhgx.learning.common.storage;

/**
 * 对象存储客户端抽象。
 */
public interface ObjectStorageClient {

    /**
     * 上传对象。
     *
     * @param objectKey 对象键
     * @param bytes     文件字节
     * @return 可访问地址
     */
    String upload(String objectKey, byte[] bytes);
}
