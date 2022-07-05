package com.choo.triple.domain.review.service;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;
import com.choo.triple.domain.review.action.ReviewAction;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final Map<ActionType, ReviewAction> actionMap;

    public ReviewService(Collection<ReviewAction> reviewActions) {
        actionMap = reviewActions.stream()
                .collect(Collectors.toMap(ReviewAction::getAction, Function.identity()));
    }

    public void actionReviewEvent(EventRequest eventRequest){
        actionMap.get(eventRequest.getAction()).action(eventRequest);
    }

}
