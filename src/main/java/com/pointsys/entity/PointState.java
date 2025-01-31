package com.pointsys.entity;

public enum PointState {
    SAVE(0), // 적립완료
    CANCEL_SAVE(1),  // 적립취소
    USE(2),  // 사용완료
    CANCEL_USE(3);  // 사용취소

    private final int value;

    PointState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PointState fromValue(int value) {
        for (PointState state : PointState.values()) {
            if (state.value == value) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid PointState value: " + value);
    }
}
