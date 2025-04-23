package site.shresthacyrus.neighborhoodhelpplatform.service;

import site.shresthacyrus.neighborhoodhelpplatform.dto.request.skill.SkillRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.skill.SkillResponseDto;

import java.util.List;

public interface SkillService {

    List<SkillResponseDto> getAllSkills();

    SkillResponseDto createSkill(SkillRequestDto skillRequestDto);
}
