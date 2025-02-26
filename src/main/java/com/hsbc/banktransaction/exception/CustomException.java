package com.hsbc.banktransaction.exception;

import com.hsbc.banktransaction.common.ResultCode;

public class CustomException extends RuntimeException {
    private final ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
    public ResultCode getResultCode() {
        return resultCode;
    }
}