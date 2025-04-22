package site.shresthacyrus.neighborhoodhelpplatform.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.UserRepository;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    public CommandLineRunner seedUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                List<User> users = List.of(
                        createUser("tanstan", "tanstan@example.com", "(111)-111-1111", "Tan", "Stan", RoleEnum.SEEKER),
                        createUser("xnkrjungg", "xnkrjungg@example.com", "(222)-222-2222", "Xnkr", "Jung", RoleEnum.HELPER),
                        createUser("admin", "admin@example.com", "(333)-333-3333", "Admin", "Root", RoleEnum.ADMIN)
                );

                userRepository.saveAll(users);
                System.out.println("🌱 Seeded sample users.");
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
