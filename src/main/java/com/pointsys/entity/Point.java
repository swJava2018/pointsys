package com.pointsys.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 포인트 ID
    @Column(nullable = false)
    private Long userId; // 사용자 ID
    @Column(nullable = false)
    private Long pointSpecId; // 포인트 정책 ID
    private int amount; // 포인트 수량
    private LocalDateTime expiryDate; // 만료일
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType type; // 타입 (MANUAL or AUTO)
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private PointState state; // 상태 (0: 적입완료, 1: 적립취소, 2: 사용완료, 3: 사용취소)
    @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public Point() {}

    public Point(Long userId, Long pointSpecId, int amount, LocalDateTime expiryDate, PointType type, PointState state) {
        this.userId = userId;
        this.pointSpecId = pointSpecId;
        this.amount = amount;
        this.expiryDate = expiryDate;
        this.type = type;
        this.state = state;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPointSpecId() {
        return pointSpecId;
    }

    public void setPointSpecId(Long pointSpecId) {
        this.pointSpecId = pointSpecId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public PointType getType() {
        return type;
    }

    public void setType(PointType type) {
        this.type = type;
    }

    public PointState getState() {
        return state;
    }

    public void setState(PointState state) {
        this.state = state;
    }
}
