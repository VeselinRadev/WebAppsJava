package com.fmi.insurance.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date paymentDate;

    @Column(nullable = false)
    @NotNull
    @EqualsAndHashCode.Include
    private Date dueDate;

    @Column(nullable = false)
    @NotNull
    private Double amount;

    // dont know how to handle this yet
    // @Column(nullable = false)
    // @NotNull
    // @Enumerated(EnumType.STRING)
    // private PaymentMethod paymentMethod;

    public Boolean isPaid() {
        return this.paymentDate != null;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id", nullable = false)
    private Insurance insurance;

}

