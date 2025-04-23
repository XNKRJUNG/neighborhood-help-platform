package site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid;

import java.math.BigDecimal;

public record BidRequestDto(
        String jobPublicId,
        BigDecimal amount,
        String message
) {
}
