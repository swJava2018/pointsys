package com.pointsys.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PointSpec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 포인트 정책 ID
    @Column(nullable = false)
    private int minSaveAmount; // 1회 적립가능한 최소 포인트 수량
    @Column(nullable = false)
    private int maxSaveAmount; // 1회 적립가능한 최대 포인트 수량
    @Column(nullable = false)
    private int minDaysFromNow; // 설정가능한 최소 만료일
    @Column(nullable = false)
    private int maxDaysFromNow; // 설정가능한 최대 만료일
    @Column(nullable = false)
    private int defaultExpiryDays; // 기본 만료일
    @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMinSaveAmount() {
        return minSaveAmount;
    }

    public void setMinSaveAmount(int minSaveAmount) {
        this.minSaveAmount = minSaveAmount;
    }

    public int getMaxSaveAmount() {
        return maxSaveAmount;
    }

    public void setMaxSaveAmount(int maxSaveAmount) {
        this.maxSaveAmount = maxSaveAmount;
    }

    public int getMinDaysFromNow() {
        return minDaysFromNow;
    }

    public void setMinDaysFromNow(int minDaysFromNow) {
        this.minDaysFromNow = minDaysFromNow;
    }

    public int getMaxDaysFromNow() {
        return maxDaysFromNow;
    }

    public void setMaxDaysFromNow(int maxDaysFromNow) {
        this.maxDaysFromNow = maxDaysFromNow;
    }

    public int getDefaultExpiryDays() {
        return defaultExpiryDays;
    }

    public void setDefaultExpiryDays(int defaultExpiryDays) {
        this.defaultExpiryDays = defaultExpiryDays;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime expiryDate) {
        this.createdAt = createdAt;
    }

    public boolean isValidAmount(int amount) {
        return getMinSaveAmount() <= amount && amount <= getMaxSaveAmount();
    }

    public boolean isValidExpiryDays(LocalDateTime expiryDate) {
        return expiryDate.isAfter(LocalDateTime.now().plusDays(getMinDaysFromNow())) || expiryDate.isBefore(LocalDateTime.now().plusDays(getMaxDaysFromNow()));
    }

    public LocalDateTime getDefaultExpiryDate() {
        return LocalDateTime.now().plusDays(getDefaultExpiryDays());
    }
}
