package com.choo.triple.domain.review.action;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;
import com.choo.triple.domain.review.dto.ReviewResponse;
import com.choo.triple.domain.review.entity.Review;
import com.choo.triple.domain.review.exception.ReviewNotFoundException;
import com.choo.triple.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewDeleteAction implements ReviewAction{
    private final ReviewRepository reviewRepository;
    @Override
    public ReviewResponse action(EventRequest eventRequest) {
        Review review = reviewRepository.findById(eventRequest.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException(eventRequest.getReviewId()));

        reviewRepository.delete(review);

        return ReviewResponse.of(review);
    }

    @Override
    public ActionType getAction() {
        return ActionType.DELETE;
    }
}
