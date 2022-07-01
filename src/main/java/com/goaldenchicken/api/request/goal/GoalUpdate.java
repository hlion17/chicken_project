package com.goaldenchicken.api.request.goal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GoalUpdate {

    @NotBlank(message = "타이틀을 입력하세요")
    private String title;

    @NotBlank(message = "타이틀을 입력하세요")
    private String description;

    @NotBlank(message = "진행상황이 없습니다.")
    private String status;

    private int vote;

    @Builder
    public GoalUpdate(String title, String description, String status, int vote) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.vote = vote;
    }
}
