package com.pointsys.repository;

import jakarta.persistence.LockModeType;
import com.pointsys.entity.PointBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointBalanceRepository extends JpaRepository<PointBalance, Long>  {
    @Query("SELECT COALESCE(SUM(pb.remainingAmount), 0) " +
            "FROM PointBalance pb JOIN pb.point p " +
            "WHERE p.userId = :userId AND p.state = 0 AND p.pointSpecId = :pointSpecId")
    Long sumAvailablePoints(@Param("userId") Long userId, @Param("pointSpecId") Long pointSpecId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT pb " +
            "FROM PointBalance pb JOIN pb.point p " +
            "WHERE p.userId = :userId AND p.expiryDate > CURRENT_TIMESTAMP AND p.state = 0 AND pb.remainingAmount > 0 " +
            "ORDER BY p.type ASC, p.expiryDate ASC")
    List<PointBalance> findAvailablePointsForUpdate(@Param("userId") Long userId);
}
