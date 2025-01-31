package com.pointsys.controller.user;

import com.pointsys.controller.user.dto.UserDto;
import com.pointsys.service.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/points")
public class UserPointController {
    private final PointService pointService;

    public UserPointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping("/use")
    public ResponseEntity<Void> usePoints(@RequestBody UserDto.UsePoint request) {
        pointService.usePoints(request.userId(), request.amount(), request.orderId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelUsage(@RequestBody UserDto.UsePoint request) {
        pointService.cancelPointUsage(request.userId(), request.orderId(), request.amount());
        return ResponseEntity.ok().build();
    }
}
