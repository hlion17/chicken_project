package com.goaldenchicken.api.repository;

import com.goaldenchicken.api.domain.goal.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
