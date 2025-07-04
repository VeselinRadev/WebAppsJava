package com.fmi.insurance.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;

import com.fmi.insurance.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fmi.insurance.dto.InsuranceRequestDto;
import com.fmi.insurance.dto.InsuranceResponseDto;
import com.fmi.insurance.dto.InsurancePatchDto;
import com.fmi.insurance.dto.InsuranceSearchParamDto;
import com.fmi.insurance.repository.InsuranceRepository;
import com.fmi.insurance.vo.InsuranceStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;

    private final CarService carService;

    private final ClientService clientService;

    private final PaymentService paymentService;

    public List<InsuranceResponseDto> getInsurances(InsuranceSearchParamDto searchParams) {
        return insuranceRepository.findInsurancesByCriteria(searchParams).stream()
            .map(InsuranceResponseDto::fromEntity)
            .toList();
    }

    public List<InsuranceResponseDto> getAllInsurances() {
        return insuranceRepository.findAll().stream()
                .map(InsuranceResponseDto::fromEntity)
                .toList();
    }

    public InsuranceResponseDto getInsuranceById(Long id) {
        return insuranceRepository.findById(id)
            .map(InsuranceResponseDto::fromEntity)
            .orElseThrow(() -> new IllegalArgumentException("Insurance not found with id: " + id));
    }

    public InsuranceResponseDto createInsurance(InsuranceRequestDto request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Insurer insurer = (Insurer) auth.getPrincipal();
        Car car = carService.getCarByPlateInternal(request.plate());

        List<Insurance> activeInsurances = insuranceRepository.findByCar_Plate(car.getPlate()).stream()
            .filter(insurance -> insurance.getStatus() == InsuranceStatus.ACTIVE || insurance.getStatus() == InsuranceStatus.INCOMING)
            .toList();

        if (ChronoUnit.DAYS.between(LocalDate.now(), request.startDate().toLocalDate()) > 30) {
            throw new IllegalArgumentException("Insurance start date must be within 30 days");
        }
        
        if (activeInsurances.size() >= 2) {
            throw new IllegalArgumentException("Car already has an active and incoming insurance");
        } else if (activeInsurances.size() == 1) {
            Insurance existingInsurance = activeInsurances.get(0);
            if (existingInsurance.getStatus() == InsuranceStatus.INCOMING) {
                throw new IllegalArgumentException("Car already has an incoming insurance");
            } else if (existingInsurance.getStatus() == InsuranceStatus.ACTIVE) {
                if (ChronoUnit.DAYS.between(LocalDate.now(), existingInsurance.getEndDate().toLocalDate()) > 30) {
                    throw new IllegalArgumentException("Car already has an active insurance that is valid for more than 30 days");
                }

                if (ChronoUnit.DAYS.between(request.startDate().toInstant(), existingInsurance.getEndDate().toLocalDate()) >= 0) {
                    throw new IllegalArgumentException("Car already has an active insurance that overlaps with the new insurance period");
                }

            }
        }

        

        Client client = clientService.getClientByUcnInternal(request.ucn());

        // generate policy number

        Insurance insurance = Insurance.builder()
            .startDate(request.startDate())
            .endDate(Date.valueOf(request.startDate().toLocalDate().plusYears(1)))
            .sticker(request.sticker())
            .greenCard(request.greenCard())
            .details(request.details())
            .car(car)
            .client(client)
            .insurer(insurer)
            .payments(new HashSet<>())
            .status(LocalDate.now().isBefore(request.startDate().toLocalDate()) ? InsuranceStatus.INCOMING : InsuranceStatus.ACTIVE)
            .build();


        insuranceRepository.save(insurance);

        List<Payment> payments = paymentService.createPayments(insurance, car, request.numberOfPayments());

        payments.forEach(insurance::addPayment);
        car.addInsurance(insurance);
        client.addInsurance(insurance);



        String policyNumber = "GO-" + String.format("%d%06d",LocalDate.now().getYear() % 100, insurance.getId());
        insurance.setPolicyNumber(policyNumber);
        insuranceRepository.save(insurance);

        return InsuranceResponseDto.fromEntity(insurance);
    }

    public InsuranceResponseDto updateInsuranceById(Long id, InsurancePatchDto request) {
        Insurance insurance = insuranceRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Insurance not found with id: " + id));

        if (insurance.getStatus() == InsuranceStatus.ANNULLED || insurance.getStatus() == InsuranceStatus.EXPIRED) {
            throw new IllegalArgumentException("Cannot update insurance with annulled or expired status");
        }

        insurance.setStatus(request.status());

        return InsuranceResponseDto.fromEntity(insuranceRepository.save(insurance));
    }
}
