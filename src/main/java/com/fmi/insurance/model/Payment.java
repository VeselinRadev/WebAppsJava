package com.fmi.insurance.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fmi.insurance.vo.PaymentMethod;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private Date paymentDate;

    @Column(nullable = false)
    @NotNull
    @EqualsAndHashCode.Include
    private Date dueDate;

    @Column(nullable = false)
    @NotNull
    private Double amount;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    @NotNull
    private boolean isPaid;

    @ManyToOne
    @JoinColumn(name = "insurance_id", nullable = false)
    private Insurance insurance;

    @EqualsAndHashCode.Include
    public String getPolicyNumber() {
        return insurance != null ? insurance.getPolicyNumber() : null;
    }

}

