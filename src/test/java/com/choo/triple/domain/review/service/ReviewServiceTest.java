package com.choo.triple.domain.review.service;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;
import com.choo.triple.domain.event.enums.ResourceType;
import com.choo.triple.domain.review.dto.ReviewResponse;
import com.choo.triple.domain.review.entity.Review;
import com.choo.triple.domain.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReviewServiceTest {
    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;

    @Nested
    @DisplayName("리뷰 이벤트는")
    class Describe_review_event{
        @Nested
        @DisplayName("저장 요청이 오면")
        class Context_with_save_request{
            EventRequest eventRequest;

            @BeforeEach
            void setUp(){
                eventRequest = prepareEventRequest(ActionType.ADD, UUID.randomUUID(), UUID.randomUUID(), "Original Review Content!");
            }

            @Test
            @DisplayName("리뷰를 저장한다.")
            void it_save_review(){
                ReviewResponse reviewResponse = reviewService.actionReviewEvent(eventRequest);

                Review review = reviewRepository.findById(reviewResponse.getId()).get();
                assertThat(review.getContent()).isEqualTo(eventRequest.getContent());
                assertThat(review.getPlaceId()).isEqualTo(eventRequest.getPlaceId());
                assertThat(review.getUserId()).isEqualTo(eventRequest.getUserId());
            }
        }

        @Nested
        @DisplayName("수정 요청이 오면")
        class Context_with_mod_request{
            EventRequest eventRequest;
            Review orgReview;

            @BeforeEach
            void setUp(){
                orgReview = prepareReview(UUID.randomUUID(), UUID.randomUUID());
                eventRequest = prepareEventRequest(ActionType.MOD, orgReview.getUserId(), orgReview.getPlaceId(), "update Review Content");
                setReviewId(eventRequest, orgReview.getId());
            }

            @Test
            @DisplayName("리뷰를 수정한다.")
            void it_mod_review(){
                reviewService.actionReviewEvent(eventRequest);
                Review review = reviewRepository.findById(orgReview.getId()).get();

                assertThat(review.getContent()).isEqualTo(eventRequest.getContent());
            }
        }

        @Nested
        @DisplayName("리뷰 삭제 요청이 오면")
        class Context_with_delete_review{
            EventRequest eventRequest;
            Review orgReview;

            @BeforeEach
            void setUp(){
                orgReview = prepareReview(UUID.randomUUID(), UUID.randomUUID());
                eventRequest = prepareEventRequest(ActionType.DELETE, orgReview.getUserId(), orgReview.getPlaceId(), "");
                setReviewId(eventRequest, orgReview.getId());
            }

            @Test
            @DisplayName("리뷰를 삭제한다.")
            void it_delete_review(){
                reviewService.actionReviewEvent(eventRequest);

                Optional<Review> review = reviewRepository.findById(orgReview.getUserId());
                assertThat(review.isPresent()).isFalse();
            }
        }
    }

    private EventRequest prepareEventRequest(ActionType action, UUID userId, UUID placeId, String content) {
        return EventRequest.builder()
                .type(ResourceType.REVIEW)
                .action(action)
                .content(content)
                .attachedPhotoIds(null)
                .userId(userId)
                .placeId(placeId)
                .build();
    }

    public Review prepareReview(UUID userId, UUID placeId){
        EventRequest eventRequest = prepareEventRequest(ActionType.ADD, userId, placeId, "original Review Content");
        return reviewRepository.save(Review.of(eventRequest));
    }

    public void setReviewId(EventRequest request, UUID id){
        ReflectionTestUtils.setField(request, "reviewId", id);
    }
}