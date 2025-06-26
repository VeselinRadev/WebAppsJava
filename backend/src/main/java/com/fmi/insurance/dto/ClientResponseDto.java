package com.fmi.insurance.dto;

import com.fmi.insurance.model.Client;

public record ClientResponseDto(
    Long id,
    String ucn,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    AddressDto address,
    Integer experienceYears
) {
    public static ClientResponseDto fromEntity(Client client) {
        return new ClientResponseDto(
                client.getId(),
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
