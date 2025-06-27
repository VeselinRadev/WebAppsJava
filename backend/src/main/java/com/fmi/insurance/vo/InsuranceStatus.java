package com.fmi.insurance.vo;

public enum InsuranceStatus {
    ACTIVE, EXPIRED, ANNULLED, INCOMING;
    public static InsuranceStatus fromString(String type) {
        return EnumUtils.fromString(InsuranceStatus.class, type);
    }
}
