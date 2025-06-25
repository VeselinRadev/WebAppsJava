package com.fmi.insurance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmi.insurance.model.Insurer;

@Repository
public interface InsurerRepository extends JpaRepository<Insurer, Long> {
    Optional<Insurer> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String email);

    //Optional<Insurer> findByInsurance_PolicyNumber(String policyNumber);
}
