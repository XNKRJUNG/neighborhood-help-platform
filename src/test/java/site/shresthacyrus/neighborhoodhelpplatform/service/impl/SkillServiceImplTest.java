package site.shresthacyrus.neighborhoodhelpplatform.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.skill.SkillRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.skill.SkillResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.exception.skill.DuplicateSkillException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.skill.SkillNotFoundException;
import site.shresthacyrus.neighborhoodhelpplatform.mapper.SkillMapper;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;
import site.shresthacyrus.neighborhoodhelpplatform.repository.SkillRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SkillMapper skillMapper;

    @InjectMocks
    private SkillServiceImpl skillService;

    @Test
    @DisplayName("Should get all skills")
    void shouldGetAllSkills() {
        Skill skill = new Skill("Plumbing");
        skill.setId(1L);
        SkillResponseDto dto = new SkillResponseDto(1L, "Plumbing", LocalDateTime.now());

        when(skillRepository.findAll()).thenReturn(List.of(skill));
        when(skillMapper.skillToSkillResponseDtoList(List.of(skill))).thenReturn(List.of(dto));

        List<SkillResponseDto> result = skillService.getAllSkills();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Plumbing");
    }

    @Test
    @DisplayName("Should create a new skill successfully")
    void shouldCreateNewSkillSuccessfully() {
        SkillRequestDto requestDto = new SkillRequestDto("Painting");
        Skill mappedSkill = new Skill("Painting");
        Skill savedSkill = new Skill("Painting");
        savedSkill.setId(2L);
        savedSkill.setCreatedAt(LocalDateTime.now());
        SkillResponseDto responseDto = new SkillResponseDto(2L, "Painting", LocalDateTime.now());

        when(skillRepository.findByNameIgnoreCase("Painting")).thenReturn(Optional.empty());
        when(skillMapper.skillRequestDtoToSkill(requestDto)).thenReturn(mappedSkill);
        when(skillRepository.save(any(Skill.class))).thenReturn(savedSkill);
        when(skillMapper.skillToSkillResponseDto(savedSkill)).thenReturn(responseDto);

        SkillResponseDto result = skillService.createSkill(requestDto);

        assertThat(result.name()).isEqualTo("Painting");
    }

    @Test
    @DisplayName("Should throw DuplicateSkillException if skill already exists")
    void shouldThrowDuplicateSkillException() {
        SkillRequestDto requestDto = new SkillRequestDto("Painting");
        Skill existingSkill = new Skill("Painting");

        when(skillRepository.findByNameIgnoreCase("Painting")).thenReturn(Optional.of(existingSkill));

        assertThatThrownBy(() -> skillService.createSkill(requestDto))
                .isInstanceOf(DuplicateSkillException.class)
                .hasMessageContaining("Skill with name Painting already exists");

        verify(skillMapper, never()).skillRequestDtoToSkill(any());
        verify(skillRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update an existing skill successfully")
    void shouldUpdateSkillSuccessfully() {
        Long skillId = 1L;
        SkillRequestDto requestDto = new SkillRequestDto("Updated Skill Name");
        Skill existingSkill = new Skill("Old Name");
        existingSkill.setId(skillId);

        Skill updatedSkill = new Skill("Updated Skill Name");
        updatedSkill.setId(skillId);

        SkillResponseDto responseDto = new SkillResponseDto(skillId, "Updated Skill Name", LocalDateTime.now());

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existingSkill));
        when(skillRepository.save(existingSkill)).thenReturn(updatedSkill);
        when(skillMapper.skillToSkillResponseDto(updatedSkill)).thenReturn(responseDto);

        SkillResponseDto result = skillService.updateSkill(skillId, requestDto);

        assertThat(result.name()).isEqualTo("Updated Skill Name");
    }

    @Test
    @DisplayName("Should throw SkillNotFoundException when updating non-existent skill")
    void shouldThrowSkillNotFoundExceptionOnUpdate() {
        when(skillRepository.findById(99L)).thenReturn(Optional.empty());

        SkillRequestDto requestDto = new SkillRequestDto("Doesn't Matter");

        assertThatThrownBy(() -> skillService.updateSkill(99L, requestDto))
                .isInstanceOf(SkillNotFoundException.class)
                .hasMessageContaining("Skill with ID 99 not found");
    }

    @Test
    @DisplayName("Should delete an existing skill successfully")
    void shouldDeleteSkillSuccessfully() {
        Long skillId = 1L;
        Skill existingSkill = new Skill("To Delete");
        existingSkill.setId(skillId);

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existingSkill));

        skillService.deleteSkill(skillId);

        verify(skillRepository).delete(existingSkill);
    }

    @Test
    @DisplayName("Should throw SkillNotFoundException when deleting non-existent skill")
    void shouldThrowSkillNotFoundExceptionOnDelete() {
        when(skillRepository.findById(88L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> skillService.deleteSkill(88L))
                .isInstanceOf(SkillNotFoundException.class)
                .hasMessageContaining("Skill with ID 88 not found");
    }
}