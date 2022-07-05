package com.choo.triple.domain.event.controller;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;
import com.choo.triple.domain.event.enums.ResourceType;
import com.choo.triple.domain.review.entity.Review;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("리뷰 작성은")
    @Transactional
    class Describe_add_review{
        @Nested
        @DisplayName("리뷰작성 요청이오면")
        class Context_with_request_add{
            EventRequest eventRequest;
            @BeforeEach
            void setUp(){
                eventRequest = EventRequest.builder()
                        .type(ResourceType.REVIEW)
                        .action(ActionType.ADD)
                        .reviewId(UUID.randomUUID())
                        .content("좋아요!")
                        .attachedPhotoIds(null)
                        .userId(UUID.randomUUID())
                        .placeId(UUID.randomUUID())
                        .build();
            }

            @Test
            @DisplayName("리뷰를 작성하고 유저의 포인트를 증가시킨다.")
            void it_add_review_and_increase_point() throws Exception{
                mockMvc.perform(post("/events")
                        .content(objectMapper.writeValueAsString(eventRequest))
                )
                        .andExpect(status().isCreated())
                        ;
            }
        }

        @Nested
        @DisplayName("한 장소에 이미 등록된 리뷰가 있다면")
        class Context_with_duplicated_review{
            EventRequest eventRequest;

            @BeforeEach
            void setUp() throws Exception{
                UUID userId = UUID.randomUUID();
                UUID placeId = UUID.randomUUID();

                prepareReview(userId, placeId);
                eventRequest = prepareEventRequest(ActionType.ADD, userId, placeId);
            }

            @Test
            @DisplayName("리뷰가 중복되었다는 에러를 반환한다.")
            void it_return_reviewDuplicatedException() throws Exception{
                mockMvc.perform(post("/events")
                        .content(objectMapper.writeValueAsString(eventRequest))
                )
                        .andExpect(status().isBadRequest())
                        ;
            }
        }
    }

    private EventRequest prepareEventRequest(ActionType action, UUID userId, UUID placeId) {
        return EventRequest.builder()
                .type(ResourceType.REVIEW)
                .action(action)
                .reviewId(UUID.randomUUID())
                .content("좋아요!")
                .attachedPhotoIds(null)
                .userId(userId)
                .placeId(placeId)
                .build();
    }

    public Review prepareReview(UUID userId, UUID placeId) throws Exception{
        EventRequest eventRequest = prepareEventRequest(ActionType.ADD, userId, placeId);
        MvcResult mvcResult = mockMvc.perform(post("/events")
                .content(objectMapper.writeValueAsString(eventRequest))
        ).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, Review.class);
    }
}