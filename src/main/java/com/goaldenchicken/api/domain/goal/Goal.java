package com.goaldenchicken.api.domain.goal;

import com.goaldenchicken.api.domain.BaseTimeEntity;
import com.goaldenchicken.api.request.goal.GoalUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Goal extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column(columnDefinition = "varchar(10) default '진행중'")
    private String status;

    @Column(columnDefinition = "int default 0")
    private int vote;

    @Builder
    public Goal(String title, String description, String status, int vote) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.vote = vote;
    }

    public void update(GoalUpdate update) {
        title = update.getTitle();
        description = update.getDescription();
        status = update.getStatus();
        vote = update.getVote();
    }
}
