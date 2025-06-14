package com.fmi.insurance.repository;

import com.fmi.insurance.model.TestModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestModel, Long> {
}