package com.fmi.insurance.vo;

public enum FuelType {
    
    GASOLINE,
    DIESEL,
    ELECTRIC,
    HYBRID,
    LPG;

    public static FuelType fromString(String type) {
        return EnumUtils.fromString(FuelType.class, type);
    }
}
