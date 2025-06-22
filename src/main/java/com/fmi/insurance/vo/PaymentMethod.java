package com.fmi.insurance.vo;

public enum PaymentMethod {
    CREDIT_CARD,
    BANK_TRANSFER,
    CASH,
    OTHER;

    public static PaymentMethod fromString(String method) {
        return EnumUtils.fromString(PaymentMethod.class, method);
    }
}
