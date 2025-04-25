package site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BidResponseDto(
        Long id,
        String jobPublicId,
        String helperUsername,
        String helperFullName,
        BigDecimal amount,
        String message,
        LocalDateTime createdAt,
        Boolean accepted
) {
}
