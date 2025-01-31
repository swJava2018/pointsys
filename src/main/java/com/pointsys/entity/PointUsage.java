package com.pointsys.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PointUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 포인트 사용 내역 ID
    @Column(nullable = false)
    private String orderId; // 주문번호 ID
    @Column(nullable = false)
    private int amount; // 포인트 수량
    @ManyToOne
    @JoinColumn(name = "point_id", nullable = false)
    private PointBalance pointBalance;
    @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public PointUsage() {}

    public PointUsage(String orderId, int amountUsed, PointBalance pointBalance) {
        this.orderId = orderId;
        this.amount = amountUsed;
        this.pointBalance = pointBalance;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isFullyRefunded() {
        return amount == 0;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public PointBalance getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(PointBalance pointBalance) {
        this.pointBalance = pointBalance;
    }
}
