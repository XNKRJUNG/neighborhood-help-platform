package site.shresthacyrus.neighborhoodhelpplatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.UserRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.UserResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.UserRepository;
import site.shresthacyrus.neighborhoodhelpplatform.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;
    public final UserRepository userRepository;

    // Get all Users
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        List<UserResponseDto> userResponseDtos = userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDtos);
    }

    @GetMapping("/debug/users/raw")
    public List<User> getRawUsers() {
        return userRepository.findAll();
    }

    // Create a User
//    @PostMapping
//    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto){
//        UserResponseDto userResponseDto = userService.createUser(userRequestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
//    }
}
