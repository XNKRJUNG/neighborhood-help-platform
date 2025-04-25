package site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BidRequestDto(

        @NotNull(message = "Bid amount is required.")
        @DecimalMin(value = "0.00", message = "Bid amount must be greater than or equal to 0.")
        BigDecimal amount,

        String message
) {
}


