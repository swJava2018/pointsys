package com.pointsys.repository;

import com.pointsys.entity.UserPointLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPointLimitRepository extends JpaRepository<UserPointLimit, Long> {
    @Query("SELECT p FROM UserPointLimit p WHERE p.id.userId = :userId AND p.id.pointSpecId = :pointSpecId")
    UserPointLimit findByUserIdAndPointSpecId(@Param("userId") Long userId, @Param("pointSpecId") Long pointSpecId);
}
