package com.fmi.insurance.dto;

import java.sql.Date;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InsuranceRequestDto(
    
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    Date startDate,

    @NotBlank(message = "Sticker number is required")
    @Size(min = 9, max = 9, message = "Sticker number must be exactly 9 characters")
    String sticker,

    @NotBlank(message = "Green card number is required")
    @Size(min = 5, max = 20)
    String greenCard,

    @Size(max = 255)
    String details,

    @NotBlank(message = "Plate is required")
    @Size(min = 7, max = 8)
    String plate,

    @NotBlank(message = "UCN is required")
    @Size(min = 10, max = 10)
    String ucn,

    @NotNull(message = "Number of payments is required")
    Integer numberOfPayments

) {
    @AssertTrue(message = "Number of payments must be 1, 2, or 4")
    public boolean isValidNumberOfPayments() {
        return numberOfPayments != null && (numberOfPayments == 1 || numberOfPayments == 2 || numberOfPayments == 4);
    }

}
