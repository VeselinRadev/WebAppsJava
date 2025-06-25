package com.fmi.insurance.model;

import java.sql.Date;
import java.util.Set;

import com.fmi.insurance.vo.InsuranceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "insurance")
@Builder
public class Insurance {
    @Id
    private String policyNumber;

    @Column(nullable = false)
    @NotNull
    private Date startDate;

    @Column(nullable = false)
    @NotNull
    private Date endDate;

    @Column(nullable = false, unique = true)
    @NotNull
    private String sticker;

    @Column(nullable = false, unique = true)
    @NotNull
    private String greenCard;

    @Column
    private String details;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InsuranceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurer_id", nullable = false)
    private Insurer insurer;

    @OneToMany(orphanRemoval = true, mappedBy = "insurance")
    private Set<Payment> payments;

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setInsurance(this);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setInsurance(null);
    }
}