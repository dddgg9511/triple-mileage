package com.choo.triple.domain.review.entity;

import com.choo.triple.common.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "review_id", columnDefinition = "BINARY(16)")
    private UUID id;

    private String content;

    private UUID userId;

    private UUID placeId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reviewId")
    List<Photo> photos;
}
