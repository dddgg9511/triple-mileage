package com.choo.triple.domain.point.repository;

import com.choo.triple.domain.point.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PointLogRepository extends JpaRepository<PointLog, UUID> {
    //List<PointLog> findByUserId(UUID userId);

    List<PointLog> findByUser_id(UUID uuid);

    List<PointLog> findByReview_id(UUID id);
}
