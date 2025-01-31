package com.pointsys.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PointBalance {
    @Id
    private Long pointId; // 포인트 ID

    @OneToOne
    @MapsId
    @JoinColumn(name = "point_id")
    private Point point;

    @Column(nullable = false)
    private int remainingAmount; // 잔여 포인트 수량

    @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public PointBalance() {}

    public PointBalance(Point point, int remainingAmount) {
        this.point = point;
        this.remainingAmount = remainingAmount;
    }

    public boolean isUsed() {
        return this.point.getAmount() != this.remainingAmount;
    }

    public Long getPointId() {
        return pointId;
    }

    public void setPointId(Long pointId) {
        this.pointId = pointId;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(int remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
}
