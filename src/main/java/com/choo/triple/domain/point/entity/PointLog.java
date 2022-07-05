package com.choo.triple.domain.point.entity;

import com.choo.triple.common.entity.BaseEntity;
import com.choo.triple.domain.point.enums.PointStatus;
import com.choo.triple.domain.point.enums.PointType;
import com.choo.triple.domain.review.entity.Review;
import com.choo.triple.domain.user.entity.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
public class PointLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_log_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PointType type;

    @Enumerated(EnumType.STRING)
    private PointStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
}
