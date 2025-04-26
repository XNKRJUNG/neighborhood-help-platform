package site.shresthacyrus.neighborhoodhelpplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.UserRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.UserResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.exception.user.DuplicateUserException;
import site.shresthacyrus.neighborhoodhelpplatform.mapper.UserMapper;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.UserRepository;
import site.shresthacyrus.neighborhoodhelpplatform.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

//    @Override
//    public UserResponseDto createUser(UserRequestDto userRequestDto) {
//        // Check whether user already exists
//        Optional<User> optionalUser = userRepository.findByUsername(userRequestDto.username());
//        if (optionalUser.isPresent()) {
//            throw new DuplicateUserException("User already exists by this username: " + userRequestDto.username());
//        }
//
//        // Map DTO to entity
//        User user = userMapper.userRequestDtoToUser(userRequestDto);
//
//        // Set hashed password
//        String hashedPassword = passwordEncoder.encode(userRequestDto.password());
//        user.setPasswordHash(hashedPassword);
//
//        // Save user
//        User savedUser = userRepository.save(user);
//
//        return userMapper.userToUserResponseDto(savedUser);
//    }

//    @Override
//    public List<UserResponseDto> findAllUsers() {
//        List<User> users = userRepository.findAll();
//        return userMapper.userToUserResponseDtoList(users);
//    }
    @Override
    public List<UserResponseDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        System.out.println("Fetched from DB: " + users.size());
        return userMapper.userToUserResponseDtoList(users);
    }

    @Override
    public UserResponseDto getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userMapper.userToUserResponseDto(user);
    }
}
