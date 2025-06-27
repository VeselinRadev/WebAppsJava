package com.fmi.insurance.dto;

import com.fmi.insurance.model.Car;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CarPatchDto(
    @Size(min = 8, max = 8, message = "Plate must be exactly 8 characters")
    String plate,

    @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
    String vin,

    String make,

    String model,

    @Max(value = 2025, message = "Year must be a valid number")
    Integer year,

    @Min(value = 500, message = "Volume must be at least 500cc")
    @Max(value = 10000, message = "Volume must be realistic")
    Integer volume,

    @Min(value = 20, message = "Power must be at least 20hp")
    @Max(value = 2000, message = "Power must be realistic")
    Integer power,

    @Min(value = 1, message = "Seats must be at least 1")
    Integer seats,

    String fuelType // should we change it to FuelType
){
    public static CarPatchDto fromEntity(Car car) {
        return new CarPatchDto(
            car.getPlate(),
            car.getVin(),
            car.getMake(),
            car.getModel(),
            car.getYear(),
            car.getVolume(),
            car.getPower(),
            car.getSeats(),
            car.getFuelType() != null ? car.getFuelType().name() : null
        );
    }
}
