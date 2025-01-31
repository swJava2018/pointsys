package com.pointsys.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class UserPointLimit {
    @EmbeddedId
    private UserPointLimitId id; // 사용자 포인트 제한 ID
    @Column(nullable = false)
    private int maxEarningLimit; // 사용자의 최대 포인트 제한 수량
    @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public UserPointLimitId getId() {
        return id;
    }

    public void setId(UserPointLimitId id) {
        this.id = id;
    }

    public int getMaxEarningLimit() {
        return maxEarningLimit;
    }

    public void setMaxEarningLimit(int maxEarningLimit) {
        this.maxEarningLimit = maxEarningLimit;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
