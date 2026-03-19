package com.dhgx.learning.common.error;

/**
 * 系统错误码定义。
 */
public enum ErrorCode {

    PARAM_INVALID("A0400", "参数不合法"),
    RESOURCE_NOT_FOUND("A0404", "资源不存在"),
    UNAUTHORIZED("A0401", "鉴权失败"),
    INTERNAL_ERROR("B0500", "系统内部错误");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
