package com.goaldenchicken.api.exception;

import org.springframework.http.HttpStatus;

public class GoalNotFound extends ApiException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";
    private final int code = HttpStatus.NOT_FOUND.value();

    public GoalNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return code;
    }
}
