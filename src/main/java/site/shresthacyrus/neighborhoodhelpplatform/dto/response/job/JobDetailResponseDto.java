package site.shresthacyrus.neighborhoodhelpplatform.dto.response.job;

import site.shresthacyrus.neighborhoodhelpplatform.dto.response.UserResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid.BidResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.skill.SkillResponseDto;

import java.math.BigDecimal;

public record JobDetailResponseDto(
        String publicId,
        String title,
        String description,
        String zipCode,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String status,
        SkillResponseDto skill,
        UserResponseDto seeker,
        BidResponseDto acceptedBid
) {}
