package com.fmi.insurance.dto;

import com.fmi.insurance.model.Insurance;

public record InsuranceDto (
    Long id,
    String policyNumber,
    String startDate,
    String endDate,
    String details
){
    public static InsuranceDto fromEntity(Insurance insurance) {
        return new InsuranceDto(
            insurance.getId(),
            insurance.getPolicyNumber(),
            insurance.getStartDate().toString(),
            insurance.getEndDate().toString(),
            insurance.getDetails() != null ? insurance.getDetails() : ""
        );
    }
}