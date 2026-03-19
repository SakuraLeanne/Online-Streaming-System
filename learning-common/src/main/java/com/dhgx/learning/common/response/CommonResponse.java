package com.dhgx.learning.common.response;

import com.dhgx.learning.common.error.ErrorCode;

import java.io.Serializable;

/**
 * 统一响应结构。
 *
 * @param <T> 业务数据类型
 */
public class CommonResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(T data) {
        CommonResponse<T> response = new CommonResponse<T>();
        response.setSuccess(true);
        response.setCode("0");
        response.setMessage("OK");
        response.setData(data);
        return response;
    }

    public static <T> CommonResponse<T> failure(ErrorCode errorCode) {
        CommonResponse<T> response = new CommonResponse<T>();
        response.setSuccess(false);
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
