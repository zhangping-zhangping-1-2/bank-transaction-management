package com.hsbc.banktransaction.common;

public enum ResultCode {
    SUCCESS(200, "成功"),
    ERROR(500, "服务器内部错误"),
    TRANSACTION_NOT_FOUND(404, "交易不存在"),
    TRANSACTION_EXIST_NOT_ADD(1001, "交易存在,不能再添加"),
    TRANSACTION_NOT_EXIST_UPDATE(1002, "交易不存在，无法修改"),
    TRANSACTION_NOT_EXIST_DELETE(1003, "交易不存在，无法删除"),
    VALIDATION_ERROR(400, "参数校验错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}