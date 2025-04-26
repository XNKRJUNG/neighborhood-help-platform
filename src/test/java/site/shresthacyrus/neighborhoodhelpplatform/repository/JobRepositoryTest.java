package site.shresthacyrus.neighborhoodhelpplatform.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;
import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    private Skill createSkill() {
        Skill skill = new Skill("Plumbing");
        return skillRepository.save(skill);
    }

    private User createSeeker() {
        User seeker = new User(
                "seeker_username",
                "seeker@example.com",
                "1112223333",
                "Seeker",
                "User",
                RoleEnum.SEEKER,
                "passwordHash"
        );
        return userRepository.save(seeker);
    }

    private Job createJob(Skill skill, User seeker, String title) {
        Job job = new Job();
        job.setPublicId(UUID.randomUUID().toString());
        job.setTitle(title);
        job.setDescription("Fix leaking pipe");
        job.setZipCode("12345");
        job.setMinPrice(BigDecimal.valueOf(50));
        job.setMaxPrice(BigDecimal.valueOf(100));
        job.setCreatedAt(LocalDateTime.now());
        job.setStatus(JobStatusEnum.OPEN);
        job.setSkill(skill);
        job.setSeeker(seeker);
        return jobRepository.save(job);
    }

    @Test
    @DisplayName("Should save and find Job by public ID")
    void shouldSaveAndFindJobByPublicId() {
        Skill skill = createSkill();
        User seeker = createSeeker();
        Job savedJob = createJob(skill, seeker, "Fix Pipe");

        Optional<Job> foundJob = jobRepository.findJobByPublicId(savedJob.getPublicId());

        assertThat(foundJob).isPresent();
        assertThat(foundJob.get().getId()).isEqualTo(savedJob.getId());
        assertThat(foundJob.get().getTitle()).isEqualTo("Fix Pipe");
    }

    @Test
    @DisplayName("Should find Job by title and seeker ID")
    void shouldFindJobByTitleAndSeekerId() {
        Skill skill = createSkill();
        User seeker = createSeeker();
        Job savedJob = createJob(skill, seeker, "Fix Door");

        Optional<Job> foundJob = jobRepository.findByTitleIgnoreCaseAndSeekerId("fix door", seeker.getId());

        assertThat(foundJob).isPresent();
        assertThat(foundJob.get().getPublicId()).isEqualTo(savedJob.getPublicId());
    }

    @Test
    @DisplayName("Should find all Jobs by seeker ID")
    void shouldFindAllJobsBySeekerId() {
        Skill skill = createSkill();
        User seeker = createSeeker();
        createJob(skill, seeker, "Fix AC");
        createJob(skill, seeker, "Fix Heater");

        Page<Job> jobPage = jobRepository.findAllBySeekerId(seeker.getId(), PageRequest.of(0, 10));

        assertThat(jobPage.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("Should count Jobs by year created")
    void shouldCountJobsByYearCreated() {
        Skill skill = createSkill();
        User seeker = createSeeker();
        createJob(skill, seeker, "Fix Fridge");

        int thisYear = LocalDateTime.now().getYear();
        long jobCount = jobRepository.countByYearCreated(thisYear);

        assertThat(jobCount).isGreaterThanOrEqualTo(1);
    }
}