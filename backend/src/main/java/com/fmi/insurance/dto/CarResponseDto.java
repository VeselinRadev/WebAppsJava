package com.fmi.insurance.dto;

import com.fmi.insurance.model.Car;

public record CarResponseDto(
    Long id,
    String plate,
    String vin,
    String make,
    String model,
    Integer year,
    Integer volume,
    Integer power,
    Integer seats,
    String fuelType,
    Integer registrationYear,
    String clientUcn,
    Long clientId
) {
    public static CarResponseDto fromEntity(Car car) {
        return new CarResponseDto(
            car.getId(),
            car.getPlate(),
            car.getVin(),
            car.getMake(),
            car.getModel(),
            car.getYear(),
            car.getVolume(),
            car.getPower(),
            car.getSeats(),
            car.getFuelType() != null ? car.getFuelType().name() : null,
            car.getRegistrationYear(),
            car.getClient().getUcn(),
            car.getClient().getId()
        );
    }
}
