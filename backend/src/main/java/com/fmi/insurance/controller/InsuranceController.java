package com.fmi.insurance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmi.insurance.dto.InsuranceRequestDto;
import com.fmi.insurance.dto.InsurancePatchDto;
import com.fmi.insurance.dto.InsuranceSearchParamDto;
import com.fmi.insurance.service.InsuranceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/insurances")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @GetMapping
    public ResponseEntity<List<InsuranceRequestDto>> getInsurances(@Valid @ModelAttribute InsuranceSearchParamDto searchParams) {
        List<InsuranceRequestDto> insurances = insuranceService.getInsurances(searchParams);
        return ResponseEntity.ok(insurances);
    }


    @PostMapping
    public ResponseEntity<InsuranceRequestDto> createInsurance(@Valid @RequestBody InsuranceRequestDto request) {
        InsuranceRequestDto createdInsurance = insuranceService.createInsurance(request);
        return ResponseEntity.ok(createdInsurance);
    }

    @GetMapping("{id}")
    public ResponseEntity<InsuranceRequestDto> getInsuranceById(@PathVariable Long id) {
        InsuranceRequestDto insurance = insuranceService.getInsuranceById(id);
        return ResponseEntity.ok(insurance);
    }

    // for anulirane
    @PatchMapping("{id}")
    public ResponseEntity<InsurancePatchDto> updateInsuranceById(@PathVariable Long id, @Valid @RequestBody InsurancePatchDto request) {
        // InsuranceDto updatedInsurance = insuranceService.updateInsuranceById(id, request);
        // return ResponseEntity.ok(updatedInsurance);
        return ResponseEntity.ok(new InsurancePatchDto()); // Placeholder for actual implementation
    }
}
