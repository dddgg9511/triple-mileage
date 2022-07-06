package com.choo.triple.domain.review.action;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;
import com.choo.triple.domain.review.dto.ReviewResponse;

public interface ReviewAction {
    ReviewResponse action(EventRequest eventRequest);

    ActionType getAction();
}
