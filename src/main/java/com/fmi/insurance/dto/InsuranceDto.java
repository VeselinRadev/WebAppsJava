package com.fmi.insurance.dto;

import com.fmi.insurance.model.Insurance;

public record InsuranceDto (
    String policyNumber,
    String startDate,
    String endDate,
    String sticker,
    String greenCard,
    String details
){
    public static InsuranceDto fromEntity(Insurance insurance) {
        return new InsuranceDto(
            insurance.getPolicyNumber(),
            insurance.getStartDate().toString(),
            insurance.getEndDate().toString(),
            insurance.getSticker(),
            insurance.getGreenCard(),
            insurance.getDetails() != null ? insurance.getDetails() : ""
        );
    }
}