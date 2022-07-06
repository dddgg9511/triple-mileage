package com.choo.triple.domain.review.exception;

import java.util.UUID;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(UUID reviewId) {
        super("리뷰를 찾을 수 없습니다. id : " + reviewId);
    }
}
