package com.fmi.insurance.dto;

import com.fmi.insurance.model.Address;

public record AddressDto(
        String city,
        String street,
        String postalCode
) {
    public static AddressDto fromEntity(Address address) {
        return new AddressDto(
                address.getCity(),
                address.getStreet(),
                address.getPostalCode()
        );
    }
}

