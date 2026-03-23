package com.dhgx.learning.media.document.exception;

/**
 * 文档转换异常。
 */
public class DocumentConvertException extends RuntimeException {

    public DocumentConvertException(String message) {
        super(message);
    }

    public DocumentConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
