package com.goaldenchicken.api.controller;

import com.goaldenchicken.api.exception.ApiException;
import com.goaldenchicken.api.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse body = ErrorResponse.builder()
                .code(BAD_REQUEST.value())
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            body.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(BAD_REQUEST.value())
                .body(body);
    }

    @ResponseBody
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> apiExceptionHandler(ApiException e) {
        ErrorResponse body = ErrorResponse.builder()
                .message(e.getMessage())
                .code(e.getStatusCode())
                .build();
        return ResponseEntity
                .status(e.getStatusCode())
                .body(body);
    }
}
