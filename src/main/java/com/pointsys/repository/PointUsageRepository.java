package com.pointsys.repository;

import jakarta.persistence.LockModeType;
import com.pointsys.entity.PointUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointUsageRepository extends JpaRepository<PointUsage, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PointUsage p WHERE p.orderId = :orderId")
    List<PointUsage> findByOrderIdForUpdate(String orderId);
}
