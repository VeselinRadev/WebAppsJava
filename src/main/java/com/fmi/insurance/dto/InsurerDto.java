package com.fmi.insurance.dto;

import com.fmi.insurance.model.Insurer;

public record InsurerDto (
    Long id,
    String username,
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
