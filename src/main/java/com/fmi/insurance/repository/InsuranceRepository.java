package com.fmi.insurance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmi.insurance.model.Insurance;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    Optional<Insurance> findByPolicyNumber(String policyNumber);

    Optional<Insurance> findByCar_Plate(String plate);

    Optional<Insurance> findByCar_Vin(String vin);

    Optional<Insurance> findByClient_Ucn(String ucn);

    Optional<Insurance> findByInsurer_Username(String username);
}
