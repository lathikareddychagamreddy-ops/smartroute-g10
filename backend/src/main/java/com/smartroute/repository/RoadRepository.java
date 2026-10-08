package com.smartroute.repository;

import com.smartroute.model.RoadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadRepository extends JpaRepository<RoadEntity, Long> {
    List<RoadEntity> findBySourceIgnoreCase(String source);
    List<RoadEntity> findByDestinationIgnoreCase(String destination);
}
