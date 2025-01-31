package com.pointsys.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PointStateConverter implements AttributeConverter<PointState, Integer> {
    @Override
    public Integer convertToDatabaseColumn(PointState pointState) {
        if (pointState == null) {
            return null;
        }
        return pointState.getValue();
    }

    @Override
    public PointState convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }
        return PointState.fromValue(value);
    }
}