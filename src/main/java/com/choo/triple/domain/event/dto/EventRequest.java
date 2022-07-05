package com.choo.triple.domain.event.dto;

import com.choo.triple.domain.event.enums.ActionType;
import com.choo.triple.domain.event.enums.ResourceType;
import com.choo.triple.domain.review.entity.Photo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class EventRequest {
    private ResourceType type;
    private ActionType action;
    private UUID reviewId;
    private String content;
    private List<UUID> attachedPhotoIds;
    private UUID userId;
    private UUID placeId;
}
