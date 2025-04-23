package site.shresthacyrus.neighborhoodhelpplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByNameIgnoreCase(String name);
}
