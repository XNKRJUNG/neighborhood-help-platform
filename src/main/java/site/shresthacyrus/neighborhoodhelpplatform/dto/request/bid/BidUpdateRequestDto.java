package site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public record BidUpdateRequestDto(
        @DecimalMin(value = "0.0", message = "Amount must be at least 0")
        BigDecimal amount,
        String message
) {}
