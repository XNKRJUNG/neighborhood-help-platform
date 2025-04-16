package site.shresthacyrus.neighborhoodhelpplatform.auth.service;

import site.shresthacyrus.neighborhoodhelpplatform.auth.dto.request.AuthenticationRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.auth.dto.request.RegisterRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.auth.dto.response.AuthenticationResponseDto;

public interface AuthenticationService {

    AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto);

    AuthenticationResponseDto register(RegisterRequestDto registerRequestDto);
}
