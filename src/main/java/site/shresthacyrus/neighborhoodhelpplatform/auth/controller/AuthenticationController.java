package site.shresthacyrus.neighborhoodhelpplatform.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.shresthacyrus.neighborhoodhelpplatform.auth.dto.request.AuthenticationRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.auth.dto.request.RegisterRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.auth.dto.response.AuthenticationResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.auth.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponseDto> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        // call service method, register(authenticationRequestDto)
        AuthenticationResponseDto authenticationResponseDto = authenticationService.register(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticateUser(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        // call a service method, authenticate(authenticationRequestDto)
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(authenticationRequestDto));
    }
}
