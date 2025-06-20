package com.fmi.insurance.model;

import java.sql.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    private String policyNumber;

    @Column(nullable = false)
    @NotNull
    private Date startDate;

    @Column(nullable = false)
    @NotNull
    private Date endDate;

    @Column
    private String details;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "insurer_id", nullable = false)
    private Insurer insurer;

    @OneToMany(mappedBy = "insurance")
    private Set<Payment> payments;
}
