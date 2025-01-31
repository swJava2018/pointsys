package com.pointsys.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserPointLimitId {
    private Long userId;
    private Long pointSpecId;
}
