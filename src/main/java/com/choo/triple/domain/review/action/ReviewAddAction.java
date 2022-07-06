package com.choo.triple.domain.review.action;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;
import com.choo.triple.domain.review.dto.ReviewResponse;
import com.choo.triple.domain.review.entity.Review;
import com.choo.triple.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewAddAction implements ReviewAction{
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewResponse action(EventRequest eventRequest) {
        return null;
    }

    @Override
    public ActionType getAction() {
        return ActionType.ADD;
    }
}
