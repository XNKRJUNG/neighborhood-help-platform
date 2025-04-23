package site.shresthacyrus.neighborhoodhelpplatform.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;
import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.JobRepository;
import site.shresthacyrus.neighborhoodhelpplatform.repository.SkillRepository;
import site.shresthacyrus.neighborhoodhelpplatform.repository.UserRepository;
import site.shresthacyrus.neighborhoodhelpplatform.util.JobIdGeneratorUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final PasswordEncoder passwordEncoder;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobIdGeneratorUtil jobIdGeneratorUtil;

    @Bean
    @Transactional
    public CommandLineRunner seedData() {
        return args -> {
            // Seed Skills
            if (skillRepository.count() == 0) {
                List<Skill> skills = List.of(
                        new Skill("Plumbing"),
                        new Skill("Electrical"),
                        new Skill("Moving Help"),
                        new Skill("Appliance Repair"),
                        new Skill("Pet Sitting"),
                        new Skill("Furniture Assembly")
                );
                skillRepository.saveAll(skills);
                System.out.println("🌱 Seeded skills.");
            }

            // Seed Users
            if (userRepository.count() == 0) {
                List<User> users = List.of(
                        createUser("tanstan", "tanstan@example.com", "(111)-111-1111", "Tan", "Stan", RoleEnum.SEEKER),
                        createUser("xnkrjungg", "xnkrjungg@example.com", "(222)-222-2222", "Xnkr", "Jung", RoleEnum.HELPER),
                        createUser("admin", "admin@example.com", "(333)-333-3333", "Admin", "Root", RoleEnum.ADMIN)
                );
                userRepository.saveAll(users);
                System.out.println("🌱 Seeded sample users.");
            }

            // Seed Jobs
            if (jobRepository.count() == 0) {
                User seeker = userRepository.findByUsername("tanstan")
                        .orElseThrow(() -> new IllegalStateException("Seeker not found"));

                Skill plumbing = skillRepository.findByNameIgnoreCase("Plumbing")
                        .orElseThrow(() -> new IllegalStateException("Plumbing skill not found"));

                Skill electrical = skillRepository.findByNameIgnoreCase("Electrical")
                        .orElseThrow(() -> new IllegalStateException("Electrical skill not found"));

                Job job1 = new Job();
                job1.setTitle("Fix leaky faucet");
                job1.setDescription("Kitchen sink faucet dripping for a week.");
                job1.setZipCode("90210");
                job1.setMinPrice(BigDecimal.valueOf(50.00));
                job1.setMaxPrice(BigDecimal.valueOf(50.00));
                job1.setCreatedAt(LocalDateTime.now());
                job1.setStatus(JobStatusEnum.OPEN);
                job1.setSeeker(seeker);
                job1.setSkill(plumbing);
                job1.setPublicId(jobIdGeneratorUtil.generateNextJobPublicId());
                jobRepository.save(job1); // Save individually to avoid publicId conflict

                Job job2 = new Job();
                job2.setTitle("Install ceiling light");
                job2.setDescription("Need help installing a new ceiling light in living room.");
                job2.setZipCode("90210");
                job2.setMinPrice(BigDecimal.valueOf(60.00));
                job2.setMaxPrice(BigDecimal.valueOf(120.00));
                job2.setCreatedAt(LocalDateTime.now());
                job2.setStatus(JobStatusEnum.OPEN);
                job2.setSeeker(seeker);
                job2.setSkill(electrical);
                job2.setPublicId(jobIdGeneratorUtil.generateNextJobPublicId());
                jobRepository.save(job2);

                System.out.println("🛠️ Seeded 2 sample jobs for seeker: " + seeker.getUsername());
            }
        };
    }

    private User createUser(String username, String email, String phone, String firstName, String lastName, RoleEnum role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setLegalFirstName(firstName);
        user.setLegalLastName(lastName);
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode("password"));
        return user;
    }
}
