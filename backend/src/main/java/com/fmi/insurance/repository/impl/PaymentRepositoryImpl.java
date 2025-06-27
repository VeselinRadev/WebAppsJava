package com.fmi.insurance.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fmi.insurance.dto.PaymentSearchParamDto;
import com.fmi.insurance.model.Payment;
import com.fmi.insurance.repository.custom.PaymentRepositoryCustom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Payment> findPaymentsByCriteria(PaymentSearchParamDto searchParams) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Payment> query = cb.createQuery(Payment.class);
        Root<Payment> payment = query.from(Payment.class);

        List<Predicate> predicates = new ArrayList<>();

        Optional.ofNullable(searchParams.insuranceId()).ifPresent(insuranceId -> 
            predicates.add(cb.equal(payment.get("insurance").get("id"), insuranceId))
        );

        Optional.ofNullable(searchParams.afterDate()).ifPresent(afterDate -> 
            predicates.add(cb.greaterThanOrEqualTo(payment.get("dueDate"), afterDate))
        );

        Optional.ofNullable(searchParams.beforeDate()).ifPresent(beforeDate -> 
            predicates.add(cb.lessThanOrEqualTo(payment.get("dueDate"), beforeDate))
        );

        
        query.select(payment).where(cb.and(predicates.toArray(new Predicate[0])));

        return em.createQuery(query).getResultList();
    }

}
