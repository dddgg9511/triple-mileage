package com.choo.triple.domain.review.action;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;
import com.choo.triple.domain.review.dto.ReviewResponse;
import com.choo.triple.domain.review.entity.Review;
import com.choo.triple.domain.review.exception.ReviewNotFoundException;
import com.choo.triple.domain.review.repository.ReviewRepository;
import com.choo.triple.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewModAction implements ReviewAction{
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public ReviewResponse action(EventRequest eventRequest) {
        Review review = reviewRepository.findById(eventRequest.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException(eventRequest.getReviewId()));

        review.updateContent(eventRequest.getContent());

        return ReviewResponse.of(review);
    }

    @Override
    public ActionType getAction() {
        return ActionType.MOD;
    }
}
