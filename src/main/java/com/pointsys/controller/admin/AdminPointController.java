package com.pointsys.controller.admin;

import com.pointsys.controller.admin.dto.AdminDto;
import com.pointsys.entity.PointType;
import com.pointsys.service.PointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/points")
public class AdminPointController {
    private final PointService pointService;

    public AdminPointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping("/save")
    public ResponseEntity<Void> savePoints(@RequestBody AdminDto.SavePoint request) {
        pointService.addPoints(request.userId(), request.amount(), PointType.MANUAL, request.expiryDate());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelPoints(@RequestBody AdminDto.CancelPoint request) {
        pointService.cancelPoints(request.pointId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updatePoints(@RequestBody AdminDto.UpdatePoint request) {
        pointService.updatePoints(request.pointId(), request.expiryDate());
        return ResponseEntity.ok().build();
    }
}
