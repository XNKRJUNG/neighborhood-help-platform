package site.shresthacyrus.neighborhoodhelpplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.skill.SkillRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.skill.SkillResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.exception.skill.DuplicateSkillException;
import site.shresthacyrus.neighborhoodhelpplatform.mapper.SkillMapper;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;
import site.shresthacyrus.neighborhoodhelpplatform.repository.SkillRepository;
import site.shresthacyrus.neighborhoodhelpplatform.service.SkillService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillMapper skillMapper;
    private final SkillRepository skillRepository;

    @Override
    public List<SkillResponseDto> getAllSkills() {
        return skillMapper.skillToSkillResponseDtoList(skillRepository.findAll());
    }

    @Override
    public SkillResponseDto createSkill(SkillRequestDto skillRequestDto) {

        // Check whether skill already exists
        skillRepository.findByNameIgnoreCase(skillRequestDto.name())
                .ifPresent(existingSkill -> {
                    throw new DuplicateSkillException("Skill with name " + skillRequestDto.name() + " already exists");
                });

        Skill mappedSkill = skillMapper.skillRequestDtoToSkill(skillRequestDto);
        mappedSkill.setCreatedAt(LocalDateTime.now());

        Skill savedSkill = skillRepository.save(mappedSkill);
        return skillMapper.skillToSkillResponseDto(savedSkill);
    }
}
