package com.fmi.insurance.repository.custom;

import java.util.List;

import com.fmi.insurance.dto.PaymentSearchParamDto;
import com.fmi.insurance.model.Payment;

public interface PaymentRepositoryCustom {

    List<Payment> findPaymentsByCriteria(PaymentSearchParamDto searchParams);

}
