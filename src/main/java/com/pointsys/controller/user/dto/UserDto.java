package com.pointsys.controller.user.dto;

public class UserDto {
    public record UsePoint(Long userId, String orderId, int amount) {}
}
