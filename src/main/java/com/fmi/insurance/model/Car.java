package com.fmi.insurance.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "car")
@Builder
public class Car {

    @Id
    private String plate;

    @Column(nullable = false, unique = true)
    @NotNull
    private String vin;

    @Column(nullable = false)
    @NotNull
    private String make;

    @Column(nullable = false)
    @NotNull
    private String model;

    @Column(nullable = false)
    @NotNull
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(orphanRemoval = true, mappedBy = "car")
    private Set<Insurance> insurances;

    void addInsurance(Insurance insurance) {
        insurances.add(insurance);
        insurance.setCar(this);
    }

    void removeInsurance(Insurance insurance) {
        insurances.remove(insurance);
        insurance.setCar(null);
    }
}