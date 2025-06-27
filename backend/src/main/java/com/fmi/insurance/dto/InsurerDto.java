package com.fmi.insurance.dto;

import com.fmi.insurance.model.Insurer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InsurerDto (
    Long id,

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    String username,

    @NotBlank(message = "Phone number is required")
    String phoneNumber,

    AddressDto address
) {
    public static InsurerDto fromEntity(Insurer insurer) {
        return new InsurerDto(
                insurer.getId(),
                insurer.getUsername(),
                insurer.getPhoneNumber(),
                insurer.getAddress() != null ? AddressDto.fromEntity(insurer.getAddress()) : null
        );
    }
}
