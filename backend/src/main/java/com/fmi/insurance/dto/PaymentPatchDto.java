package com.fmi.insurance.dto;

import com.fmi.insurance.vo.PaymentMethod;

public record PaymentPatchDto(
    Boolean paid,
    PaymentMethod paymentMethod
) 
{}
