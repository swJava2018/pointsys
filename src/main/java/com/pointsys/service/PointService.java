package com.pointsys.service;

import com.pointsys.entity.*;
import com.pointsys.exception.*;
import com.pointsys.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PointService {
    private final Long defaultPointSpecId = 1L;

    private final UserPointLimitRepository userPointLimitRepository;
    private final PointSpecRepository pointSpecRepository;
    private final PointRepository pointRepository;
    private final PointBalanceRepository pointBalanceRepository;
    private final PointUsageRepository pointUsageRepository;

    public PointService(UserPointLimitRepository userPointLimitRepository, PointSpecRepository pointSpecRepository, PointRepository pointRepository, PointBalanceRepository pointBalanceRepository, PointUsageRepository pointUsageRepository) {
        this.userPointLimitRepository = userPointLimitRepository;
        this.pointSpecRepository = pointSpecRepository;
        this.pointRepository = pointRepository;
        this.pointBalanceRepository = pointBalanceRepository;
        this.pointUsageRepository = pointUsageRepository;
    }

    @Transactional
    public void addPoints(Long userId, int amount, PointType type, LocalDateTime expiryDate) {
        PointSpec pointSpec = getPointSpecFromCache();
        if (!pointSpec.isValidAmount(amount)) {
            throw new PointSaveLimitExceededException("사용자 포인트 1회 적립 범위 초과");
        }
        if (expiryDate == null) {
            expiryDate = pointSpec.getDefaultExpiryDate();
        } else if (!pointSpec.isValidExpiryDays(expiryDate)) {
            throw new PointExpLimitExceededException("사용자 포인트 적립 만료일 범위 초과");
        }

        long ownedAmount = pointBalanceRepository.sumAvailablePoints(userId, pointSpec.getId());
        UserPointLimit userPointLimit = getUserPointLimitFromCache(userId, pointSpec.getId());

        if (ownedAmount + amount > userPointLimit.getMaxEarningLimit()) {
            throw new PointBalanceLimitExceededException("사용자 최대 보유가능 포인트 한도 초과");
        }

        Point point = new Point(userId, pointSpec.getId(), amount, expiryDate, type, PointState.SAVE);
        pointRepository.save(point);

        PointBalance pointBalance = new PointBalance(point, amount);
        pointBalanceRepository.save(pointBalance);
    }

    @Cacheable(value = "pointSpecCache", key = "'pointSpec:' + #pointSpecId")
    public PointSpec getPointSpecFromCache() {
        return pointSpecRepository.findById(defaultPointSpecId).
                orElseThrow(PointSpecNotFoundException::new);
    }

    @Cacheable(value = "userPointLimitCache", key = "'userPointLimit:' + #userId + ':' + #pointSpecId")
    public UserPointLimit getUserPointLimitFromCache(Long userId, Long pointSpecId) {
        return userPointLimitRepository.findByUserIdAndPointSpecId(userId, pointSpecId);
    }

    @Transactional
    public void cancelPoints(Long pointId) {
        PointBalance balance = pointBalanceRepository.findById(pointId)
                .orElseThrow(PointNotFoundException::new);

        if (!balance.isUsed()) {
            throw new PointAlreadyUsedException("사용자 포인트가 일부 사용되어 적립 취소 불가");
        }

        Point point = balance.getPoint();
        point.setState(PointState.CANCEL_SAVE);
        pointRepository.save(point);
    }

    @Transactional
    public void updatePoints(Long pointId, LocalDateTime expiryDate) {
        PointBalance balance = pointBalanceRepository.findById(pointId)
                .orElseThrow(PointNotFoundException::new);

        Point point = balance.getPoint();
        point.setExpiryDate(expiryDate);
        pointRepository.save(point);
    }

    @Transactional
    public void usePoints(Long userId, int amount, String orderId) {
        // 사용 가능한 포인트 목록 조회
        List<PointBalance> availableBalances = pointBalanceRepository.findAvailablePointsForUpdate(userId);

        int remaining = amount;
        List<PointUsage> usagesToAdd = new ArrayList<>();

        for (PointBalance balance : availableBalances) {
            if (remaining <= 0) break;

            // 적립된 포인트 중 사용될 포인트 계산
            int used = Math.min(balance.getPoint().getAmount(), remaining);

            balance.setRemainingAmount(balance.getRemainingAmount() - used);

            // 적립된 포인트 중 사용된 포인트 기록
            PointUsage usage = new PointUsage(orderId, used, balance);
            usagesToAdd.add(usage);

            remaining -= used;
        }

        if (remaining > 0) {
            throw new PointInsufficientBalance("사용될 포인트 부족");
        }

        if (!availableBalances.isEmpty()) {
            pointBalanceRepository.saveAll(availableBalances);
        }

        if (!usagesToAdd.isEmpty()) {
            pointUsageRepository.saveAll(usagesToAdd);
        }

        Point point = new Point(userId, defaultPointSpecId, amount, null, PointType.MANUAL, PointState.USE);
        pointRepository.save(point);
    }

    @Transactional
    public void cancelPointUsage(Long userId, String orderId, int amountToCancel) {
        // (특정 상품구매시) 사용된 포인트 내역 조회
        List<PointUsage> usages = pointUsageRepository.findByOrderIdForUpdate(orderId);

        int remaining = amountToCancel;

        List<PointBalance> balancesToUpdate = new ArrayList<>();
        List<PointUsage> usageHistoriesToUpdate = new ArrayList<>();

        for (PointUsage usage : usages) {
            if (remaining <= 0) break;

            if (usage.isFullyRefunded()) continue;

            int refundAmount = Math.min(usage.getAmount(), remaining);

            PointBalance balance = usage.getPointBalance();
            Point point = balance.getPoint();
            if (point.getExpiryDate().isBefore(LocalDateTime.now())) {
                // 만료 기간이 지난 경우, 새 포인트를 적립
                addPoints(userId, refundAmount, PointType.AUTO, null);
            } else {
                // 만료 기간이 지나지 않은 경우, 기존 포인트를 갱신
                balance.setRemainingAmount(balance.getRemainingAmount() + refundAmount);
                balancesToUpdate.add(balance);
            }

            // 사용 포인트 내역 갱신
            usage.setAmount(usage.getAmount() - refundAmount);
            usageHistoriesToUpdate.add(usage);

            remaining -= refundAmount;
        }

        if (remaining > 0) {
            throw new PointInsufficientBalance("취소될 포인트 부족");
        }

        if (!balancesToUpdate.isEmpty()) {
            pointBalanceRepository.saveAll(balancesToUpdate);
        }

        if (!usageHistoriesToUpdate.isEmpty()) {
            pointUsageRepository.saveAll(usageHistoriesToUpdate);
        }

        Point point = new Point(userId, defaultPointSpecId, amountToCancel, null, PointType.MANUAL, PointState.CANCEL_USE);
        pointRepository.save(point);
    }
}
