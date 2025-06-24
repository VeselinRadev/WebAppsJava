package com.fmi.insurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fmi.insurance.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    Optional<Car> findByPlate(String plate);

    Optional<Car> findByVin(String vin);

    List<Car> findByClient_Ucn(String ucn);

    @Query("SELECT c FROM Car c JOIN c.insurances i WHERE i.policyNumber = :policyNumber")
    Optional<Car> findByInsurances_PolicyNumber(@Param("policyNumber")String policyNumber);
}
