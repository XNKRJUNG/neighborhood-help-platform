package site.shresthacyrus.neighborhoodhelpplatform.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.UserResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.mapper.UserMapper;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should return list of UserResponseDto when findAllUsers called")
    void shouldReturnListOfUserResponseDto() {
        // Arrange: create dummy User and UserResponseDto
        User user = new User(
                "john_doe",
                "john@example.com",
                "1234567890",
                "John",
                "Doe",
                RoleEnum.SEEKER,
                "passwordHash"
        );

        UserResponseDto userResponseDto = new UserResponseDto(
                1L,
                "john_doe",
                "john@example.com",
                "12345678",
                "John",
                "Doe",
                RoleEnum.SEEKER,
                false,
                false,
                false
        );

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.userToUserResponseDtoList(List.of(user))).thenReturn(List.of(userResponseDto));

        // Act: call the method
        List<UserResponseDto> result = userService.findAllUsers();

        // Assert: verify result
        assertThat(result).hasSize(1);
        assertThat(result.get(0).username()).isEqualTo("john_doe");
        assertThat(result.get(0).email()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void shouldReturnEmptyListWhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        when(userMapper.userToUserResponseDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<UserResponseDto> result = userService.findAllUsers();

        assertThat(result).isEmpty();
    }
}