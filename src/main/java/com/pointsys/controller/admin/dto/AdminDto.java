package com.pointsys.controller.admin.dto;

import java.time.LocalDateTime;

public class AdminDto {
    public record SavePoint(Long userId, int amount, LocalDateTime expiryDate) {}
    public record CancelPoint(Long pointId) {}
    public record UpdatePoint(Long pointId, LocalDateTime expiryDate) {}
}
