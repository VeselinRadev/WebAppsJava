package com.fmi.insurance.dto;

import java.sql.Date;

import lombok.Builder;

@Builder
public record InsuranceSearchParamDto (
    String vin,
    String plate,
    String ucn,
    String username,
    Date beforeDate,
    Date afterDate
){}
