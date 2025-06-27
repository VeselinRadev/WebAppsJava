package com.fmi.insurance.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fmi.insurance.dto.PaymentResponseDto;
import com.fmi.insurance.dto.InsurancePatchDto;
import com.fmi.insurance.dto.PaymentPatchDto;
import com.fmi.insurance.dto.PaymentSearchParamDto;
import com.fmi.insurance.model.Car;
import com.fmi.insurance.model.Insurance;
import com.fmi.insurance.model.Payment;
import com.fmi.insurance.repository.PaymentRepository;
import com.fmi.insurance.vo.InsuranceStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final CarService carService;

    private final InsuranceService insuranceService;

    public List<PaymentResponseDto> getPayments(PaymentSearchParamDto searchParams) {
        return paymentRepository.findPaymentsByCriteria(searchParams).stream()
                .map(PaymentResponseDto::fromEntity)
                .toList();
    }

    public PaymentResponseDto getPayment(Long id) {
        return paymentRepository.findById(id)
                .map(PaymentResponseDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + id));
    }

    List<Payment> createPayments(Insurance insurance, Car car, Integer numberOfPayments) {
        
        List<Payment> payments = new ArrayList<>();
        List<Date> dueDates = generateDueDates(numberOfPayments);

        for (int i = 0; i < numberOfPayments; i++) {
            Payment payment = Payment.builder()
                .insurance(insurance)
                .dueDate(dueDates.get(i))
                .paymentDate(i == 0 ? Date.valueOf(LocalDate.now()) : null)
                .amount(carService.getCarPrice(car) / numberOfPayments)
                .build();

            payments.add(payment);
        }

        paymentRepository.saveAll(payments);

        return payments;
    }

    public PaymentResponseDto updatePayment(Long id, PaymentPatchDto request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + id));

        Insurance insurance = payment.getInsurance();
        if (insurance.getStatus() != InsuranceStatus.ACTIVE) {
            throw new IllegalArgumentException("Cannot update payment for insurance that is not active");
        }

        Set<Payment> payments = insurance.getPayments();

        if (payments.stream().anyMatch(payment1 -> !payment1.isPaid() && payment1.getId() != id && payment1.getDueDate().before(payment.getDueDate()))) {
            throw new IllegalArgumentException("Cannot mark payment as paid because there are unpaid payments with earlier due dates.");
        }

        Optional.ofNullable(request.paid()).ifPresent(paid -> {
            if (paid) {
                payment.setPaymentDate(Date.valueOf(LocalDate.now()));
            }
        });

        paymentRepository.save(payment);
        return PaymentResponseDto.fromEntity(payment);
    }

    private List<Date> generateDueDates(Integer numberOfPayments) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();

        dates.add(today);
        
        if (numberOfPayments == 2) {
            dates.add(today.plusMonths(6));
        } else if (numberOfPayments == 4) {
            dates.add(today.plusMonths(3));
            dates.add(today.plusMonths(6));
            dates.add(today.plusMonths(9));
        }

        return dates.stream()
                .map(Date::valueOf)
                .toList();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkPayments() {
        LocalDate today = LocalDate.now().plusDays(15); 
        PaymentSearchParamDto searchParams = PaymentSearchParamDto.builder()
                .afterDate(Date.valueOf(today))
                .build();

        paymentRepository.findPaymentsByCriteria(searchParams).stream()
                .filter(payment -> !payment.isPaid())
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
