package com.fmi.insurance.dto;

import com.fmi.insurance.model.Insurance;
import com.fmi.insurance.vo.InsuranceStatus;

import lombok.Builder;

@Builder
public record InsurancePatchDto (
    InsuranceStatus status
){
    public static InsurancePatchDto fromEntity(Insurance insurance) {
        return new InsurancePatchDto(
            insurance.getStatus()
        );
    }
}