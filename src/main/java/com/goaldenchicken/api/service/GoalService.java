package com.goaldenchicken.api.service;

import com.goaldenchicken.api.domain.goal.Goal;
import com.goaldenchicken.api.exception.GoalNotFound;
import com.goaldenchicken.api.repository.GoalRepository;
import com.goaldenchicken.api.request.goal.GoalCreate;
import com.goaldenchicken.api.request.goal.GoalSearch;
import com.goaldenchicken.api.request.goal.GoalUpdate;
import com.goaldenchicken.api.response.GoalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    public void write(GoalCreate goalCreate) {
        goalRepository.save(goalCreate.toEntity());
    }

    public GoalSearch get(Long id) {
        Goal result = goalRepository.findById(id)
                .orElseThrow(GoalNotFound::new);
        return GoalSearch.builder()
                .id(result.getId())
                .title(result.getTitle())
                .description(result.getDescription())
                .status(result.getStatus())
                .vote(result.getVote())
                .build();
    }

    public List<GoalResponse> getAll() {
        return goalRepository.findAll().stream()
                .map(GoalResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(Long id, GoalUpdate updateParam) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(GoalNotFound::new);
        // Problem: Goal의 모든 필드를 update 파라미터로 넘겨줘야 하는 문제
        goal.update(updateParam);
    }

    @Transactional
    public void delete(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(GoalNotFound::new);

        goalRepository.delete(goal);
    }
}
