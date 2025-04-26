package site.shresthacyrus.neighborhoodhelpplatform.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

@DataJpaTest
class SkillRepositoryTest {

    @Autowired
    private SkillRepository skillRepository;

    @Test
    @DisplayName("Should save and find Skill by name")
    void shouldSaveAndFindSkillByName() {
        Skill skill = new Skill("Plumbing");
        Skill savedSkill = skillRepository.save(skill);

        Optional<Skill> foundSkill = skillRepository.findByNameIgnoreCase("Plumbing");

        assertThat(foundSkill).isPresent();
        assertThat(foundSkill.get().getId()).isEqualTo(savedSkill.getId());
        assertThat(foundSkill.get().getName()).isEqualTo("Plumbing");
    }

    @Test
    @DisplayName("Should return empty when Skill not found by name")
    void shouldReturnEmptyWhenSkillNotFoundByName() {
        Optional<Skill> foundSkill = skillRepository.findByNameIgnoreCase("NonExistentSkill");

        assertThat(foundSkill).isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when saving duplicate Skill name")
    void shouldThrowExceptionOnDuplicateSkillName() {
        Skill skill1 = new Skill("Electrician");
        skillRepository.save(skill1);

        Skill duplicateSkill = new Skill("Electrician");

        assertThatThrownBy(() -> skillRepository.saveAndFlush(duplicateSkill))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("Should find Skill by ID after saving")
    void shouldFindSkillById() {
        Skill skill = new Skill("Carpentry");
        Skill savedSkill = skillRepository.save(skill);

        Optional<Skill> foundSkill = skillRepository.findById(savedSkill.getId());

        assertThat(foundSkill).isPresent();
        assertThat(foundSkill.get().getName()).isEqualTo("Carpentry");
    }

    @Test
    @DisplayName("Should return empty when finding Skill by invalid ID")
    void shouldReturnEmptyForInvalidSkillId() {
        Optional<Skill> foundSkill = skillRepository.findById(999L);

        assertThat(foundSkill).isEmpty();
    }
}