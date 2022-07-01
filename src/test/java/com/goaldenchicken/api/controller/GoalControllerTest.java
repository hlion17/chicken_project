package com.goaldenchicken.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goaldenchicken.api.domain.goal.Goal;
import com.goaldenchicken.api.domain.goal.GoalStatus;
import com.goaldenchicken.api.repository.GoalRepository;
import com.goaldenchicken.api.request.goal.GoalCreate;
import com.goaldenchicken.api.request.goal.GoalUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "api.goaldenchicken.com", uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    GoalRepository goalRepository;

    @BeforeEach
    void clear() {
        goalRepository.deleteAll();
    }

    @Test
    @DisplayName("목표 생성")
    void createGoal() throws Exception {
        // given
        String title = "테스트 제목";
        String description = "테스트 설명";

        GoalCreate request = GoalCreate.builder()
                .title(title)
                .description(description)
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        this.mockMvc.perform(post("/api/goals")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                )
                .andDo(document("goal-create",
                        requestFields(
                                fieldWithPath("title").description("목표명"),
                                fieldWithPath("description").description("목표 설명"),
                                fieldWithPath("status").description("목표 진행 상황"),
                                fieldWithPath("vote").description("투표수")
                        )
                ));

        assertEquals(1L, goalRepository.count());
        List<Goal> results = goalRepository.findAll();
        assertEquals(title, results.get(0).getTitle());
        assertEquals(description, results.get(0).getDescription());
    }

    @ParameterizedTest(name = "{index} {displayName}")
    @DisplayName("목표 생성 필수값 입력하지 않을 때")
    @NullAndEmptySource
    void createGoalError(String testParam) throws Exception {
        // given
        GoalCreate request = GoalCreate.builder()
                .title(testParam)
                .description(testParam)
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        this.mockMvc.perform(post("/api/goals")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest()
                        , jsonPath("$.code").exists()
                        , jsonPath("$.message").exists()
                        , jsonPath("$.validation").exists()
                );
    }

    @Test
    @DisplayName("목표 단건 조회")
    void getGoal() throws Exception {
        // given
        String title = "테스트 제목";
        String description = "테스트 설명";

        Goal data = Goal.builder()
                .title(title)
                .description(description)
                .build();

        goalRepository.save(data);

        // expected

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/goals/{goalId}", data.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.title").value(title)
                        , jsonPath("$.description").value(description)
                        , jsonPath("$.status").value("진행중")
                        , jsonPath("$.vote").value(0)
                )
                .andDo(document("goal-search-one",
                        pathParameters(
                                parameterWithName("goalId").description("목표 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("조회된 목표ID"),
                                fieldWithPath("title").description("조회된 목표명"),
                                fieldWithPath("description").description("조회된 설명"),
                                fieldWithPath("status").description("조회된 진행상태"),
                                fieldWithPath("vote").description("조회된 목표명")
                        )
                ));

    }

    @Test
    @DisplayName("존재하지 않는 목표 조회")
    void goalNotFoundError() throws Exception {
        // given
        long notExistId = 999;

        // expected
        this.mockMvc.perform(get("/api/goals/{goalId}", notExistId))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound()
                        , jsonPath("$.code").value(404)
                        , jsonPath("$.message").value("존재하지 않는 글입니다.")
                )
                .andDo(document("found-error",
                        responseFields(
                                fieldWithPath("code").description("상태코드"),
                                fieldWithPath("message").description("에러메시지"),
                                fieldWithPath("validation").description("에러 필드").optional()
                        )
                ));
    }

    @Test
    @DisplayName("목표 리스트 조회")
    void findAllGoals() throws Exception {
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

        // expected
        this.mockMvc.perform(get("/api/goals")
                        )
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                        , jsonPath("$.length()", is(10))
                );

    }

    @Test
    @DisplayName("목표 수정")
    void updateGoal() throws Exception {
        // given
        String title = "테스트 목표명";
        String description = "테스트 목표 설명";

        Goal data = Goal.builder()
                .title(title)
                .description(description)
                .build();

        goalRepository.save(data);

        GoalUpdate updatedData = GoalUpdate.builder()
                .title("수정된 목표명")
                .description("수정된 목표 설명")
                .status(GoalStatus.SUCCESS.getValue())
                .vote(1)
                .build();

        String json = objectMapper.writeValueAsString(updatedData);

        // expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/goals/{goalId}", data.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(document("goal-update",
                        pathParameters(
                                parameterWithName("goalId").description("목표 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("수정 할 목표명"),
                                fieldWithPath("description").description("수정 할 목표 설명"),
                                fieldWithPath("status").description("수정할 진행상태"),
                                fieldWithPath("vote").description("수정할 목표명")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 목표 수정")
    void updateNotExistGoal() throws Exception {
        // given
        long notExistId = 999;

        GoalUpdate updatedData = GoalUpdate.builder()
                .title("수정된 목표명")
                .description("수정된 목표 설명")
                .status(GoalStatus.SUCCESS.getValue())
                .vote(1)
                .build();

        String json = objectMapper.writeValueAsString(updatedData);

        // expected
        this.mockMvc.perform(patch("/api/goals/{goalId}", notExistId)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    @DisplayName("목표 삭제")
    void deleteGoal() throws Exception {
        // given
        Goal goal = Goal.builder()
                .title("테스트 목표명")
                .description("테스트 목표 설명")
                .build();

        goalRepository.save(goal);

        // expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/goals/{goalId}", goal.getId()))
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                ).andDo(document("goal-delete",
                        pathParameters(
                                parameterWithName("goalId").description("목표 ID")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 목표 삭제")
    void deleteNotExistGoal() throws Exception {
        // given
        long notExistId = 999;

        // expected
        this.mockMvc.perform(delete("/api/goals/{goalId}", notExistId))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound()
                );
    }

}