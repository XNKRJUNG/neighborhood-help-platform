package site.shresthacyrus.neighborhoodhelpplatform.dto.request.job;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record JobUpdateRequestDto(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Zip code is required")
        String zipCode,

        @NotNull(message = "Minimum price is required")
        @Min(value = 0, message = "Minimum price must be at least 0")
        BigDecimal minPrice,

        @Min(value = 0, message = "Maximum price must be at least 0")
        BigDecimal maxPrice,

        @NotNull(message = "Skill ID is required")
        Long skillId

) {}
