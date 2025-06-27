package com.fmi.insurance.dto;

import com.fmi.insurance.model.Insurance;
import com.fmi.insurance.vo.InsuranceStatus;

import java.util.Date;

public record InsuranceResponseDto(
    Long id,
    String policyNumber,
    Date startDate,
    Date endDate,
    String sticker,
    String greenCard,
    String details,
    InsuranceStatus status
) {
    public static InsuranceResponseDto fromEntity(Insurance insurance) {
        return new InsuranceResponseDto(
            insurance.getId(),
            insurance.getPolicyNumber(),
            insurance.getStartDate(),
            insurance.getEndDate(),
            insurance.getSticker(),
            insurance.getGreenCard(),
            insurance.getDetails(),
            insurance.getStatus()
        );
    }
}
