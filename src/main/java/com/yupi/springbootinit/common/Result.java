package com.yupi.springbootinit.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
public class Result<T> implements Serializable {
    private int code;
    private String tip;
    private T data;
    public static <T> Result<T> success(){
        Result<T> result = new Result<T>();
        result.code = 0;
        return result;
    }
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<T>();
        result.data = data;
        result.code =0;
        return result;
    }
    public static <T> Result<T> error(String tip){
        Result<T> result = new Result<T>();
        result.tip = tip;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<T>();
        result.tip = message;
        result.code = code;
        return result;
    }
}
