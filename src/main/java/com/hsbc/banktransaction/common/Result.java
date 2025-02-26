package com.hsbc.banktransaction.common;

public class Result<T> {
    private int code;
    private String message;
    private T data;

    // 添加默认无参构造函数
    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }
    // 修改 error 方法，使其可以接受额外的错误信息
    public static <T> Result<T> error(ResultCode resultCode, String errorMessage) {
        return new Result<>(resultCode.getCode(), errorMessage, null);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}