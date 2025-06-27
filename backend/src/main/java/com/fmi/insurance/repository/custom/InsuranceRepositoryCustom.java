package com.fmi.insurance.repository.custom;

import java.util.List;

import com.fmi.insurance.dto.InsuranceSearchParamDto;
import com.fmi.insurance.model.Insurance;

public interface InsuranceRepositoryCustom {

    public List<Insurance> findInsurancesByCriteria(InsuranceSearchParamDto searchParams);
}
