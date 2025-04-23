package site.shresthacyrus.neighborhoodhelpplatform.dto.request.skill;

import jakarta.validation.constraints.NotBlank;

public record SkillRequestDto(
        @NotBlank(message = "Skill name is required.")
        String name
) {
}
