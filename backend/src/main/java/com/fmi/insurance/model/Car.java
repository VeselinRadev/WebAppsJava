package com.fmi.insurance.model;

import java.util.Set;

import com.fmi.insurance.vo.FuelType;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
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

    @Column(nullable = false)
    @NotNull
    private Integer volume;

    @Column(nullable = false)
    @NotNull
    private Integer power;

    @Column(nullable = false)
    @NotNull
    Integer seats;

    @Column(nullable = false)
    @NotNull
    private Integer registrationYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private FuelType fuelType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(orphanRemoval = true, mappedBy = "car")
    private Set<Insurance> insurances;

    public void addInsurance(Insurance insurance) {
        insurances.add(insurance);
        insurance.setCar(this);
    }

    public void removeInsurance(Insurance insurance) {
        insurances.remove(insurance);
        insurance.setCar(null);
    }
}