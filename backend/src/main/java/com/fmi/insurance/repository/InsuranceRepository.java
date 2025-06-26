package com.fmi.insurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmi.insurance.model.Insurance;
import com.fmi.insurance.repository.custom.InsuranceRepositoryCustom;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long>, InsuranceRepositoryCustom {
    Optional<Insurance> findByPolicyNumber(String policyNumber);

    List<Insurance> findByCar_Plate(String plate);

    List<Insurance> findByCar_Vin(String vin);

    List<Insurance> findByClient_Ucn(String ucn);

    List<Insurance> findByInsurer_Username(String username);
}
