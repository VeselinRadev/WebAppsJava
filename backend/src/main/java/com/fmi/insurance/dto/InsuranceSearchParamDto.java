package com.fmi.insurance.dto;

import java.sql.Date;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record InsuranceSearchParamDto (

    @Size(min = 8, max = 8, message = "Plate must be exactly 8 characters")
    String plate,

    @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
    String vin,

    @Size(min = 10, max = 10, message = "UCN must be exactly 10 characters")
    String ucn,

    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    String username,

    Date beforeDate,
    Date afterDate
){}
