package site.shresthacyrus.neighborhoodhelpplatform.dto.response.skill;

import java.time.LocalDateTime;

public record SkillResponseDto(
        Long id,
        String name,
        LocalDateTime createdAt
) {
}
