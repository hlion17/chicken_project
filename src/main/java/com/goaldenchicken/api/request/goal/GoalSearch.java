package com.goaldenchicken.api.request.goal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalSearch {

    private Long id;
    private String title;
    private String description;
    private String status;
    private int vote;

    @Builder
    public GoalSearch(Long id, String title, String description, String status, int vote) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.vote = vote;
    }
}
