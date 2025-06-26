package com.fmi.insurance.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public record CarRequestDto(
    @NotBlank(message = "Plate is required")
    @Size(min = 8, max = 8, message = "Plate must be exactly 8 characters")
    String plate,

    @NotBlank(message = "VIN is required")
    @Size(min = 17, max = 17, message = "VIN must be exactly 17 characters")
    String vin,

    @NotBlank(message = "Make is required")
    String make,

    @NotBlank(message = "Model is required")
    String model,

    @NotNull(message = "Year is required")
    @Max(value = 2025, message = "Year must be a valid number")
    Integer year,

    @NotNull(message = "Volume is required")
    @Min(value = 500, message = "Volume must be at least 500cc")
    @Max(value = 10000, message = "Volume must be realistic")
    Integer volume,

    @NotNull(message = "Power is required")
    @Min(value = 20, message = "Power must be at least 20hp")
    @Max(value = 2000, message = "Power must be realistic")
    Integer power,

    @NotNull(message = "Seats is required")
    @Min(value = 1, message = "Seats must be at least 1")
    Integer seats,

    @NotBlank(message = "Fuel type is required")
    String fuelType
){}
