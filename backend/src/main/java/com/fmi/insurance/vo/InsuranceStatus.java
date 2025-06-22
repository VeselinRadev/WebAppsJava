package com.fmi.insurance.vo;

public enum InsuranceStatus {
    ACTIVE, SUSPENDED, DEACTIVATED;
    public static InsuranceStatus fromString(String type) {
        return EnumUtils.fromString(InsuranceStatus.class, type);
    }
}
