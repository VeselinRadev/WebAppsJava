package com.fmi.insurance.dto;

import java.sql.Date;

import com.fmi.insurance.model.Payment;
import com.fmi.insurance.vo.PaymentMethod;

public record PaymentResponseDto (
    Long id,
    Date paymentDate,
    Date dueDate,
    Double amount,
    PaymentMethod method,
    Boolean isPaid
) {
    public static PaymentResponseDto fromEntity(Payment payment) {
        return new PaymentResponseDto(
            payment.getId(),
            payment.getPaymentDate(),
            payment.getDueDate(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.isPaid()
        );
    }
}
