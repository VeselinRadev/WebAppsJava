package com.fmi.insurance.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fmi.insurance.dto.InsuranceSearchParamDto;
import com.fmi.insurance.model.Insurance;
import com.fmi.insurance.repository.custom.InsuranceRepositoryCustom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class InsuranceRepositoryImpl implements InsuranceRepositoryCustom {

    @PersistenceContext
    private EntityManager em;    

    @Override
    public List<Insurance> findInsurancesByCriteria(InsuranceSearchParamDto searchParams) {

        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Insurance> cq = cb.createQuery(Insurance.class);
        Root<Insurance> insuranceRoot = cq.from(Insurance.class);

        List<Predicate> predicates = new ArrayList<>();


        Optional.ofNullable(searchParams.vin())
            .ifPresent(vin -> predicates.add(cb.equal(insuranceRoot.get("car").get("vin"), vin)));

        Optional.ofNullable(searchParams.plate())
            .ifPresent(plate -> predicates.add(cb.equal(insuranceRoot.get("car").get("plate"), plate)));

        Optional.ofNullable(searchParams.ucn())
            .ifPresent(ucn -> predicates.add(cb.equal(insuranceRoot.get("client").get("ucn"), ucn)));

        Optional.ofNullable(searchParams.username())
            .ifPresent(username -> predicates.add(cb.equal(insuranceRoot.get("insurer").get("username"), username)));

        Optional.ofNullable(searchParams.beforeDate())
            .ifPresent(beforeDate -> predicates.add(cb.lessThanOrEqualTo(insuranceRoot.get("startDate"), beforeDate)));

        Optional.ofNullable(searchParams.afterDate())
            .ifPresent(afterDate -> predicates.add(cb.greaterThanOrEqualTo(insuranceRoot.get("startDate"), afterDate)));

        cq.select(insuranceRoot)
            .where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
        
    }

}
