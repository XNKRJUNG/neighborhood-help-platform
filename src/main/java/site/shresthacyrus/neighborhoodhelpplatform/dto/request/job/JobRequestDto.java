package site.shresthacyrus.neighborhoodhelpplatform.dto.request.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JobRequestDto(

        @NotBlank(message = "Title cannot be blank/empty/null")
        String title,

        String description,

        @NotBlank(message = "Zipcode cannot be blank/empty/null")
        String zipCode,

        @NotNull(message = "Minimum price is required")
        Double minPrice,

        Double maxPrice,

        @NotNull(message = "Please select a skill/category")
        Long skillId

) {}
