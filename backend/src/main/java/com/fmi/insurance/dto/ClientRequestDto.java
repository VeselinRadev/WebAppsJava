package com.fmi.insurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClientRequestDto(
    @NotBlank(message = "UCN is required")
    @Size(min = 10, max = 10, message = "UCN must be exactly 10 characters")
    String ucn,

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 30, message = "First name must be between 3 and 30 characters")
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 30, message = "Last must be between 3 and 30 characters")
    String lastName,

    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    String phoneNumber,

    AddressDto address,

    Integer experienceYears
) {}
