package com.smartroute.repository;

import com.smartroute.model.RouteHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteHistoryRepository extends JpaRepository<RouteHistoryEntity, Long> {
    List<RouteHistoryEntity> findTop50ByOrderByTimestampDesc();
    List<RouteHistoryEntity> findByUserIdOrderByTimestampDesc(Long userId);
}
