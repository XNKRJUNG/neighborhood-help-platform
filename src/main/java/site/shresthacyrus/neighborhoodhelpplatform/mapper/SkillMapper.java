package site.shresthacyrus.neighborhoodhelpplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.skill.SkillRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.skill.SkillResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SkillMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "jobs", ignore = true)
    Skill skillRequestDtoToSkill(SkillRequestDto dto);

    SkillResponseDto skillToSkillResponseDto(Skill skill);

    List<SkillResponseDto> skillToSkillResponseDtoList(List<Skill> skills);

}
