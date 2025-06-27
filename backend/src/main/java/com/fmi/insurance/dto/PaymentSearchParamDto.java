package com.fmi.insurance.dto;

import java.sql.Date;

import lombok.Builder;

@Builder
public record PaymentSearchParamDto(
    Long insuranceId,
    Date afterDate,
    Date beforeDate
) {
}
