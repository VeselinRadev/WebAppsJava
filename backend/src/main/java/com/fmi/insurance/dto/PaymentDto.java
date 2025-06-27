package com.fmi.insurance.dto;

import com.fmi.insurance.model.Payment;

public record PaymentDto (
    Long id,
    String paymentDate,
    Double amount,
    String method,
    Boolean isPaid
) {
    public static PaymentDto fromEntity(Payment payment) {
        return new PaymentDto(
            payment.getId(),
            payment.getPaymentDate().toString(),
            payment.getAmount(),
            payment.getPaymentMethod().toString(),
            payment.isPaid()
        );
    }
}
