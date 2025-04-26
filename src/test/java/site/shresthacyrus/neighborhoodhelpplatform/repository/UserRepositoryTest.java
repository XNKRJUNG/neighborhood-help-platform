package site.shresthacyrus.neighborhoodhelpplatform.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and find User by username")
    void shouldSaveAndFindUserByUsername() {
        User user = new User(
                "john_doe",
                "john@example.com",
                "1234567890",
                "John",
                "Doe",
                RoleEnum.SEEKER,
                "hashedpassword123"
        );

        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("john_doe");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Should find User by email")
    void shouldFindUserByEmail() {
        User user = new User(
                "jane_doe",
                "jane@example.com",
                "0987654321",
                "Jane",
                "Doe",
                RoleEnum.HELPER,
                "hashedpassword456"
        );

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("jane@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("jane_doe");
    }

    @Test
    @DisplayName("Should find User by phone")
    void shouldFindUserByPhone() {
        User user = new User(
                "sam_smith",
                "sam@example.com",
                "5551234567",
                "Sam",
                "Smith",
                RoleEnum.HELPER,
                "hashedpassword789"
        );

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByPhone("5551234567");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("sam_smith");
    }

    @Test
    @DisplayName("Should return empty when User not found")
    void shouldReturnEmptyWhenUserNotFound() {
        Optional<User> foundUser = userRepository.findByUsername("non_existent_user");

        assertThat(foundUser).isEmpty();
    }
}