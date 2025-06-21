package com.fmi.insurance.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fmi.insurance.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUcn(String ucn);

    @Query("SELECT c FROM Client c JOIN c.cars ca WHERE ca.plate = :plate")
    Optional<Client> findByCars_Plate(@Param("plate")String plate);

    @Query("SELECT c FROM Client c JOIN c.cars ca where ca.vin = :vin")
    Optional<Client> findByCars_Vin(@Param("vin")String vin);

    @Query("SELECT c FROM Client c JOIN c.insurances i WHERE i.policyNumber = :policyNumber")
    Optional<Client> findByInsurances_PolicyNumber(@Param("policyNumber")String policyNumber);
}
