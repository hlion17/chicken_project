package com.goaldenchicken.api.request.goal;

import com.goaldenchicken.api.domain.goal.Goal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class GoalCreate {

    @NotBlank(message = "목표명은 필수값입니다.")
    private String title;
    @NotBlank(message = "목표설명은 필수값입니다.")
    private String description;
    private String status;
    private int vote;

    @Builder
    public GoalCreate(String title, String description, String status, int vote) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.vote = vote;
    }

    public Goal toEntity() {
        return Goal.builder()
                .title(title)
                .description(description)
                .status(status)
                .vote(vote)
                .build();
    }
}
