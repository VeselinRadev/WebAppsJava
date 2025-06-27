package com.fmi.insurance.dto;

import java.sql.Date;

public record PaymentSearchParamDto(
    Long insuranceId,
    Date afterDate,
    Date beforeDate
) {
}
