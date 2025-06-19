package com.fmi.insurance.dto;

import com.fmi.insurance.model.Car;

public record CarDto(
    Long id,
    String plate,
    String vin,
    String make,
    String model,
    Integer year
){
    public static CarDto fromEntity(Car car) {
        return new CarDto(
            car.getId(),
            car.getPlate(),
            car.getVin(),
            car.getMake(),
            car.getModel(),
            car.getYear()
        );
    }
}
