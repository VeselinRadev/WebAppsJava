package com.fmi.insurance.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmi.insurance.model.Payment;
import com.fmi.insurance.repository.custom.PaymentRepositoryCustom;
import com.fmi.insurance.vo.PaymentMethod;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {
    List<Payment> findByInsurance_PolicyNumber(String policyNumber);

    List<Payment> findByIsPaid(boolean isPaid);

    // get all payments(paid or unpaid) from today until specific date
    // dueDate >= date
    List<Payment> findByIsPaidAndDueDateGreaterThanEqual(boolean isPaid, Date date);

    // dueDate < date
    List<Payment> findByIsPaidFalseAndDueDateLessThan(Date dueDate);

    // get unpaid payments by policy number sorted by due date
    List<Payment> findByIsPaidFalseAndInsurance_PolicyNumberOrderByDueDateAsc(String policyNumber);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

}
