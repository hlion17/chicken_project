package com.goaldenchicken.api.service;

import com.goaldenchicken.api.domain.goal.Goal;
import com.goaldenchicken.api.domain.goal.GoalStatus;
import com.goaldenchicken.api.repository.GoalRepository;
import com.goaldenchicken.api.request.goal.GoalCreate;
import com.goaldenchicken.api.request.goal.GoalSearch;
import com.goaldenchicken.api.request.goal.GoalUpdate;
import com.goaldenchicken.api.response.GoalResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GoalServiceTest {

    @Autowired
    GoalRepository goalRepository;

    @Autowired
    GoalService goalService;
    @BeforeEach
    void clear() {
        goalRepository.deleteAll();
    }

    @Test
    @DisplayName("목표 작성")
    void save() {
        // given
        String title = "테스트 목표명";
        String description = "테스트 목표 설명";

        GoalCreate data = GoalCreate.builder()
                .title(title)
                .description(description)
                .build();

        // when
        goalService.write(data);

        // then
        assertEquals(1L, goalRepository.count());
        List<Goal> results = goalRepository.findAll();
        assertEquals(title, results.get(0).getTitle());
        assertEquals(description, results.get(0).getDescription());
    }

    @Test
    @DisplayName("목표 단건 조회")
    void findOneGoal() {
        // given
        String title = "테스트 목표명";
        String description = "테스트 목표 설명";

        Goal data = Goal.builder()
                .title(title)
                .description(description)
                .build();

        goalRepository.save(data);

        // when
        GoalSearch result = goalService.get(data.getId());

        // then
        assertEquals(data.getTitle(), result.getTitle());
        assertEquals(data.getDescription(), result.getDescription());
    }

    @Test
    @DisplayName("목표 리스트 조회")
    void findAllGoal() {
        // given
        List<Goal> dataList = IntStream.range(0, 10)
                .mapToObj(i -> Goal.builder()
                        .title("테스트 목표명" + i)
                        .description("테스트 목표 설명" + i)
                        .status(GoalStatus.RUNNING.getValue())
                        .vote(0)
                        .build()
                ).collect(Collectors.toList());

        goalRepository.saveAll(dataList);

        // when
        List<GoalResponse> results = goalService.getAll();

        // then
        assertEquals(10L, results.size());
    }

    @Test
    @DisplayName("목표 업데이트")
    void updateGoal() {
        // given
        String title = "테스트 목표명";
        String description = "테스트 목표 설명";

        String updatedTitle = "수정된 목표명";
        String updatedDescription = "수정된 목표명";
        String updatedStatus = GoalStatus.SUCCESS.getValue();
        int updatedVote = 1;

        Goal originalData = Goal.builder()
                .title(title)
                .description(description)
                .build();

        goalRepository.save(originalData);

        GoalUpdate updatedData = GoalUpdate.builder()
                .title(updatedTitle)
                .description(updatedDescription)
                .status(GoalStatus.SUCCESS.getValue())
                .vote(1)
                .build();

        // when
        goalService.update(originalData.getId(), updatedData);

        // then
        Goal result = goalRepository.findById(originalData.getId())
                .orElseThrow(IllegalArgumentException::new);
        assertAll(
                () -> assertEquals(updatedTitle, result.getTitle()),
                () -> assertEquals(updatedDescription, result.getDescription()),
                () -> assertEquals(updatedStatus, result.getStatus()),
                () -> assertEquals(updatedVote, result.getVote())
        );
    }

    @Test
    @DisplayName("목표 삭제")
    void deleteGoal() {
        // given
        Goal goal = Goal.builder()
                .title("테스트 목표명")
                .description("테스트 목표 설명")
                .build();

        goalRepository.save(goal);

        // when
        goalService.delete(goal.getId());

        // then
        assertEquals(0, goalRepository.count());
    }
}