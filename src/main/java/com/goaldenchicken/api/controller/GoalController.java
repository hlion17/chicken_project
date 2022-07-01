package com.goaldenchicken.api.controller;

import com.goaldenchicken.api.request.goal.GoalCreate;
import com.goaldenchicken.api.request.goal.GoalSearch;
import com.goaldenchicken.api.request.goal.GoalUpdate;
import com.goaldenchicken.api.response.GoalResponse;
import com.goaldenchicken.api.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GoalController {

    private final GoalService goalService;

    @PostMapping("/goals")
    public void saveGoal(@RequestBody @Valid GoalCreate params) {
        goalService.write(params);
    }

    @GetMapping("/goals/{goalId}")
    public ResponseEntity<GoalSearch> getGoal(@PathVariable(name = "goalId") Long id) {
        GoalSearch result = goalService.get(id);
        return ResponseEntity.status(200)
                .body(result);
    }

    @GetMapping("/goals")
    public List<GoalResponse> getAllGoals() {
        return goalService.getAll();
    }

    @PatchMapping("/goals/{goalId}")
    public void updateGoal(@PathVariable(name = "goalId") Long id, @RequestBody @Valid GoalUpdate updateParam) {
        goalService.update(id, updateParam);
    }

    @DeleteMapping("/goals/{goalId}")
    public void deleteGoal(@PathVariable(name = "goalId") Long id) {
        goalService.delete(id);
    }

}
