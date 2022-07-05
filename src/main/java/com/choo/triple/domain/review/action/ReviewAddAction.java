package com.choo.triple.domain.review.action;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;
import org.springframework.stereotype.Component;

@Component
public class ReviewAddAction implements ReviewAction{
    @Override
    public void action(EventRequest eventRequest) {

    }

    @Override
    public ActionType getAction() {
        return ActionType.ADD;
    }
}
