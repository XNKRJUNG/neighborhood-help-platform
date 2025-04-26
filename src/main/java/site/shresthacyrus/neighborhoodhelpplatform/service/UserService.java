package site.shresthacyrus.neighborhoodhelpplatform.service;

import org.springframework.security.core.Authentication;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.UserRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {

//    UserResponseDto createUser(UserRequestDto userRequestDto);
    List<UserResponseDto> findAllUsers();
    UserResponseDto getCurrentUser(Authentication authentication);

}
