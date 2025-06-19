package com.fmi.insurance.vo;

public enum PaymentMethod {
    CREDIT_CARD,
    BANK_TRANSFER,
    CASH,
    OTHER;

    public static PaymentMethod fromString(String method) {
        if (method == null || method.isEmpty()) {
            return null;
        }
        try {
            return PaymentMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid payment method: " + method);
        }
    }
}
