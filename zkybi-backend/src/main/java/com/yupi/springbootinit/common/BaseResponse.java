package com.yupi.springbootinit.common;

import java.io.Serializable;
import lombok.Data;

/**
 * 通用返回类
 *
 * @param <T> 泛型类型，表示返回数据的类型
 */
@Data
public class BaseResponse<T> implements Serializable {

    // 响应状态码
    private int code;

    // 响应数据
    private T data;

    // 响应消息
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
