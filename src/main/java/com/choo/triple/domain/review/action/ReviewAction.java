package com.choo.triple.domain.review.action;

import com.choo.triple.domain.event.dto.EventRequest;
import com.choo.triple.domain.event.enums.ActionType;

public interface ReviewAction {
    void action(EventRequest eventRequest);

    ActionType getAction();
}
