package com.fmi.insurance.scheduler;

import com.fmi.insurance.dto.InsurancePatchDto;
import com.fmi.insurance.dto.PaymentSearchParamDto;
import com.fmi.insurance.model.Insurance;
import com.fmi.insurance.model.Payment;
import com.fmi.insurance.repository.InsuranceRepository;
import com.fmi.insurance.repository.PaymentRepository;
import com.fmi.insurance.repository.custom.PaymentRepositoryCustom;
import com.fmi.insurance.service.CarService;
import com.fmi.insurance.service.InsuranceService;
import com.fmi.insurance.vo.InsuranceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class InsuranceStatusScheduler {
    private final InsuranceRepository insuranceRepository;
    private final PaymentRepository paymentRepository;
    private final CarService carService;
    private final InsuranceService insuranceService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkInsurances() {
        LocalDate today = LocalDate.now();
        List<Insurance> insurances = insuranceRepository.findByStatus(InsuranceStatus.ACTIVE).stream()
                .filter(insurance -> insurance.getEndDate().toLocalDate().isBefore(today))
                .map(insurance -> {
                    insurance.setStatus(InsuranceStatus.EXPIRED);
                    return insurance;
                })
                .toList();

        insuranceRepository.saveAll(insurances);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkPayments() {
        LocalDate today = LocalDate.now().plusDays(15);
        PaymentSearchParamDto searchParams = PaymentSearchParamDto.builder()
                .afterDate(Date.valueOf(today))
                .build();

        paymentRepository.findPaymentsByCriteria(searchParams).stream()
                .filter(payment -> !payment.getIsPaid())
                .map(Payment::getInsurance)
                .distinct()
                .peek(insurance -> {
                    InsurancePatchDto insurancePatchDto = InsurancePatchDto.builder()
                            .status(InsuranceStatus.ANNULLED)
                            .build();

                    insuranceService.updateInsuranceById(insurance.getId(), insurancePatchDto);
                });
    }
}
