package com.choo.triple.domain.review.action;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;
import com.choo.triple.domain.review.dto.ReviewResponse;
import org.springframework.stereotype.Component;

@Component
public class ReviewModAction implements ReviewAction{
    @Override
    public ReviewResponse action(EventRequest eventRequest) {
        return null;
    }

    @Override
    public ActionType getAction() {
        return ActionType.MOD;
    }
}
