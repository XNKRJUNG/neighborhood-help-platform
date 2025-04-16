package site.shresthacyrus.neighborhoodhelpplatform.dto.response;

import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;

public record UserResponseDto(

        String username,
        String email,
        String phone,
        String legalFirstName,
        String legalLastName,
        RoleEnum role,
        Boolean isMobileVerified,
        Boolean isBackgroundVerified,
        Boolean isPaymentVerified
) {
}
