package com.choo.triple.domain.review.dto;

import com.choo.triple.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {
    private UUID id;
    private UUID placeId;
    private UUID userId;
    private String content;

    public static ReviewResponse of(Review review){
        return ReviewResponse.builder()
                .id(review.getId())
                .placeId(review.getPlaceId())
                .userId(review.getUserId())
                .build();
    }
}
