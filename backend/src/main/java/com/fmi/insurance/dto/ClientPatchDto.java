package com.fmi.insurance.dto;

import com.fmi.insurance.model.Client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClientPatchDto(
    @Size(min = 10, max = 10, message = "UCN must be exactly 10 characters")
    String ucn,

    @Size(min = 3, max = 30, message = "First name must be between 3 and 30 characters")
    String firstName,

    @Size(min = 3, max = 30, message = "Last must be between 3 and 30 characters")
    String lastName,

    @Email(message = "Invalid email format")
    String email,

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    String phoneNumber,

    AddressDto address,

    Integer experienceYears
) {
    public static ClientPatchDto fromEntity(Client client) {
        return new ClientPatchDto(
                client.getUcn(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getAddress() != null ? AddressDto.fromEntity(client.getAddress()) : null,
                client.getExperienceYears()
        );
    }
}
