package site.shresthacyrus.neighborhoodhelpplatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.skill.SkillRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.skill.SkillResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.service.impl.SkillServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillServiceImpl skillService;

    @GetMapping
    public ResponseEntity<List<SkillResponseDto>> getAllSkills(){
        List<SkillResponseDto> skillResponseDtoList = skillService.getAllSkills();
        return ResponseEntity.status(HttpStatus.OK).body(skillResponseDtoList);
    }

    // 🔐 Admin only: Create a new skill
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SkillResponseDto> createSkill(@Valid @RequestBody SkillRequestDto skillRequestDto){
        SkillResponseDto createdSkill = skillService.createSkill(skillRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkill);
    }
}
