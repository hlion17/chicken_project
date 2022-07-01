package com.goaldenchicken.api.domain.goal;

public enum GoalStatus {
    RUNNING("진행중"), SUCCESS("성공"), FAIL("실패");

    private final String status;

    GoalStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }

}
