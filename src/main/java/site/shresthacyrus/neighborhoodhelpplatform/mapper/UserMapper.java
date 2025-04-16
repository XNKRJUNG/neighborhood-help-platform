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

    // Map UserRequestDto → User (exclude passwordHash – set manually later)
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "passwordHash", ignore = true)
//    @Mapping(target = "isMobileVerified", ignore = true)
//    @Mapping(target = "isBackgroundVerified", ignore = true)
//    @Mapping(target = "isPaymentVerified", ignore = true)
//    @Mapping(target = "jobs", ignore = true)
//    @Mapping(target = "bids", ignore = true)
//    @Mapping(target = "writtenReviews", ignore = true)
//    @Mapping(target = "receivedReviews", ignore = true)
//    @Mapping(target = "skill", ignore = true)
//    @Mapping(target = "photos", ignore = true)
    User userRequestDtoToUser(UserRequestDto userRequestDto);

    @Mapping(target = "password", ignore = true) // password can't be reversed
    UserRequestDto userToUserRequestDto(User user);

    UserResponseDto userToUserResponseDto(User user);

    List<UserResponseDto> userToUserResponseDtoList(List<User> users);
}
