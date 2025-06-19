package com.fmi.insurance.dto;

import com.fmi.insurance.model.Client;

public record ClientDto(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    AddressDto address
) {
    public static ClientDto fromEntity(Client client) {
        return new ClientDto(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhoneNumber(),
                client.getAddress() != null ? AddressDto.fromEntity(client.getAddress()) : null
        );
    }
}
