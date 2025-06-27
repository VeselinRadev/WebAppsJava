package com.fmi.insurance.service;

import java.security.cert.X509CRLSelector;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.fmi.insurance.dto.PaymentDto;
import com.fmi.insurance.dto.PaymentPatchDto;
import com.fmi.insurance.dto.PaymentSearchParamDto;
import com.fmi.insurance.model.Car;
import com.fmi.insurance.model.Insurance;
import com.fmi.insurance.model.Payment;
import com.fmi.insurance.repository.PaymentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final CarService carService;

    public List<PaymentDto> getPayments(PaymentSearchParamDto searchParams) {
        return paymentRepository.findPaymentsByCriteria(searchParams).stream()
                .map(PaymentDto::fromEntity)
                .toList();
    }

    public PaymentDto getPayment(Long id) {
        return paymentRepository.findById(id)
                .map(PaymentDto::fromEntity)
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

            insurance.addPayment(payment);
            payments.add(payment);
        }


        return payments;
    }

    public PaymentDto updatePayment(Long id, PaymentPatchDto patch) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + id));

        Set<Payment> payments = payment.getInsurance().getPayments();

        if (payments.stream().anyMatch(payment1 -> !payment1.isPaid() && payment1.getId() != id && payment1.getDueDate().before(payment.getDueDate()))) {
            throw new IllegalArgumentException("Cannot mark payment as paid because there are unpaid payments with earlier due dates.");
        }

        Optional.ofNullable(patch.paid()).ifPresent(paid -> {
            if (paid) {
                payment.setPaymentDate(Date.valueOf(LocalDate.now()));
            }
        });

        paymentRepository.save(payment);
        return PaymentDto.fromEntity(payment);
    }

    private List<Date> generateDueDates(Integer numberOfPayments) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < numberOfPayments; i++) {
            dates.add(today.plusMonths(i * 3));
        }

        return dates.stream()
                .map(Date::valueOf)
                .toList();
    }
}
