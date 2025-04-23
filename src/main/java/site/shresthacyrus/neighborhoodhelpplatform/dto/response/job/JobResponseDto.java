package site.shresthacyrus.neighborhoodhelpplatform.dto.response.job;

import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;

import java.time.LocalDateTime;

public record JobResponseDto(
        String publicId,
        String title,
        String description,
        String zipCode,
        Double minPrice,
        Double maxPrice,
        JobStatusEnum status,
        String skillName,
        LocalDateTime createdAt,
        String seekerFullName
) {}
