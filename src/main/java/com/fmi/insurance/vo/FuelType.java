package com.fmi.insurance.vo;

public enum FuelType {
    
    GASOLINE,
    DIESEL,
    ELECTRIC,
    HYBRID,
    LPG;

    public static FuelType fromString(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        try {
            return FuelType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid fuel type: " + type);
        }
    }
}
