package com.goaldenchicken.api.response;

import com.goaldenchicken.api.domain.goal.Goal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoalResponse {

    private Long id;
    private String title;
    private String description;
    private String status;
    private int vote;

    @Builder
    public GoalResponse(Long id, String title, String description, String status, int vote) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.vote = vote;
    }

    public GoalResponse(Goal goal) {
        this.id = goal.getId();
        this.title = goal.getTitle();
        this.description = goal.getDescription();
        this.status = goal.getStatus();
        this.vote = goal.getVote();
    }
}
