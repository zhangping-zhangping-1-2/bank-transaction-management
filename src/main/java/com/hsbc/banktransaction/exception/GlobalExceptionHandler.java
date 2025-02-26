package com.hsbc.banktransaction.exception;

import com.hsbc.banktransaction.common.Result;
import com.hsbc.banktransaction.common.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Result<Void>> handleCustomException(CustomException e) {
        ResultCode resultCode = e.getResultCode();
        return new ResponseEntity<>(Result.error(resultCode), HttpStatus.valueOf(resultCode.getCode()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Result<Void>> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(Result.error(ResultCode.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // 新增对 MethodArgumentNotValidException 的处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getDefaultMessage()).append("; ");
        }
        return new ResponseEntity<>(Result.error(ResultCode.VALIDATION_ERROR, errorMessage.toString()), HttpStatus.BAD_REQUEST);
    }
}