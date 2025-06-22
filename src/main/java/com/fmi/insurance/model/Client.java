package com.fmi.insurance.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "client")
@Builder
public class Client {

    @Id
    private String ucn;

    @Column(nullable = false)
    @NotNull
    private String firstName;

    @Column(nullable = false)
    @NotNull
    private String lastName;

    @Column
    private String email;

    @Column(nullable = false)
    @NotNull
    private String phoneNumber;

    @Column
    @Embedded
    private Address address;

    @Column(nullable = false)
    @NotNull
    private Integer experienceYears;

    @OneToMany(orphanRemoval = true, mappedBy = "client")
    private Set<Car> cars;

    @OneToMany(mappedBy = "client")
    private Set<Insurance> insurances;
    
    void addCar(Car car) {
        cars.add(car);
        car.setClient(this);
    }

    void removeCar(Car car) {
        cars.remove(car);
        car.setClient(null);
    }

    void addInsurance(Insurance insurance) {
        insurances.add(insurance);
        insurance.setClient(this);
    }

    void removeInsurance(Insurance insurance) {
        insurances.remove(insurance);
        insurance.setClient(null);
    }

}