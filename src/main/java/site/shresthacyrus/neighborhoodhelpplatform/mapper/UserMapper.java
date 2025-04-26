package site.shresthacyrus.neighborhoodhelpplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.UserRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.UserResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User userRequestDtoToUser(UserRequestDto userRequestDto);
    @Mapping(target = "password", ignore = true)
    UserRequestDto userToUserRequestDto(User user);
    UserResponseDto userToUserResponseDto(User user);
    List<UserResponseDto> userToUserResponseDtoList(List<User> users);
}

