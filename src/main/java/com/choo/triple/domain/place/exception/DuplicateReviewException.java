package com.choo.triple.domain.place.exception;

import java.util.UUID;

public class DuplicateReviewException extends RuntimeException{
    public DuplicateReviewException(UUID userId){
        super("해당 계정으로 이미 등록된 리뷰가 존재합니다. id : " + userId);
    }
}
