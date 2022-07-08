package com.choo.triple.domain.review.action;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;
import com.choo.triple.domain.event.enums.ResourceType;
import com.choo.triple.domain.place.exception.DuplicateReviewException;
import com.choo.triple.domain.point.entity.PointLog;
import com.choo.triple.domain.point.enums.PointType;
import com.choo.triple.domain.point.repository.PointLogRepository;
import com.choo.triple.domain.review.dto.ReviewResponse;
import com.choo.triple.domain.review.entity.Review;
import com.choo.triple.domain.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ReviewAddActionTest {
    @Autowired
    ReviewAddAction reviewAddAction;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    PointLogRepository pointLogRepository;

    @Nested
    @DisplayName("리뷰 저장은")
    class Describe_save{
        @Nested
        @DisplayName("장소에 이미 등록된 리뷰가 있다면")
        class Context_with_duplicate_review_in_place{
            EventRequest eventRequest;

            @BeforeEach
            void setUp(){
                Review orgReview = prepareReview(UUID.randomUUID(), UUID.randomUUID());
                eventRequest = prepareEventRequest(ActionType.ADD, orgReview.getUserId(), orgReview.getPlaceId(), "content!!");
            }

            @Test
            @DisplayName("리뷰가 중복되었다는 예외를 던진다.")
            void it_throw_duplicateReviewException(){
                assertThatThrownBy(() -> reviewAddAction.action(eventRequest))
                        .isInstanceOf(DuplicateReviewException.class);
            }
        }

        @Nested
        @DisplayName("이미지가 없고 내용이 1글자 이상이면")
        class Context_with_non_photo_and_valid_content{
            EventRequest eventRequest;

            @BeforeEach
            void setUp(){
                eventRequest = prepareEventRequest(ActionType.ADD, UUID.randomUUID(), UUID.randomUUID(), "content!!");

                prepareReview(UUID.randomUUID(), eventRequest.getPlaceId());
            }
            @Test
            @DisplayName("1 포인트를 적립한다.")
            void it_takes_one_point(){
                reviewAddAction.action(eventRequest);
                List<PointLog> pointLogList = pointLogRepository.findByUser_id(eventRequest.getUserId());
                assertThat(pointLogList.size()).isEqualTo(1);
                assertThat(pointLogList.get(0).getPoint()).isEqualTo(1);

            }
        }

        @Nested
        @DisplayName("이미지가 있다면")
        class Context_with_photo{
            EventRequest eventRequest;

            @BeforeEach
            void setUp(){
                eventRequest = EventRequest.builder()
                        .type(ResourceType.REVIEW)
                        .action(ActionType.ADD)
                        .userId(UUID.randomUUID())
                        .placeId(UUID.randomUUID())
                        .content("")
                        .attachedPhotoIds(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()))
                        .build();

                prepareReview(UUID.randomUUID(), eventRequest.getPlaceId());
            }

            @Test
            @DisplayName("이미지 마다 1 포인트를 추가로 적립한다.")
            void it_takes_one_point_per_photo(){
                ReviewResponse review = reviewAddAction.action(eventRequest);
                List<PointLog> pointLogList = pointLogRepository.findByReview_id(review.getId());
                PointLog photoLog = pointLogList.stream()
                        .filter(log -> log.getType() == PointType.PHOTO)
                        .findFirst().get();

                assertThat(photoLog.getPoint()).isEqualTo(eventRequest.getAttachedPhotoIds().size());
            }
        }

        @Nested
        @DisplayName("장소에 등록된 리뷰가 없다면")
        class Context_with_first_review{
            EventRequest eventRequest;

            @BeforeEach
            void setUp(){
                eventRequest = EventRequest.builder()
                        .type(ResourceType.REVIEW)
                        .action(ActionType.ADD)
                        .userId(UUID.randomUUID())
                        .placeId(UUID.randomUUID())
                        .content("")
                        .attachedPhotoIds(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()))
                        .build();

                prepareReview(UUID.randomUUID(), eventRequest.getPlaceId());
            }

            @Test
            @DisplayName("첫 리뷰 보너스를 추가로 적립한다.")
            void it_takes_bonus_point(){
                ReviewResponse review = reviewAddAction.action(eventRequest);
                List<PointLog> pointLogList = pointLogRepository.findByReview_id(review.getId());
                Optional<PointLog> bonusLog = pointLogList.stream()
                        .filter(log -> log.getType() == PointType.PHOTO)
                        .findFirst();
                assertThat(bonusLog.isPresent()).isTrue();
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
}