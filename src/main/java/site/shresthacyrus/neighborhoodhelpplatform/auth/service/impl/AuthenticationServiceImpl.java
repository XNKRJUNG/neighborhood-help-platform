package site.shresthacyrus.neighborhoodhelpplatform.auth.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.shresthacyrus.neighborhoodhelpplatform.auth.dto.request.AuthenticationRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.auth.dto.request.RegisterRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.auth.dto.response.AuthenticationResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.auth.service.AuthenticationService;
import site.shresthacyrus.neighborhoodhelpplatform.config.JwtService;
import site.shresthacyrus.neighborhoodhelpplatform.exception.user.DuplicateUserException;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
   public AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto){
       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       authenticationRequestDto.username(),
                       authenticationRequestDto.password()
               )
       );
       User user = (User) authentication.getPrincipal();

       String token = jwtService.generateToken(user);
       return new AuthenticationResponseDto(token);
   }

   @Override
   public AuthenticationResponseDto register(RegisterRequestDto registerRequestDto) {

       // Validate uniqueness
       if (userRepository.findByUsername(registerRequestDto.username()).isPresent()) {
           throw new DuplicateUserException("Username already taken: " + registerRequestDto.username());
       }
       if (userRepository.findByEmail(registerRequestDto.email()).isPresent()) {
           throw new DuplicateUserException("Email already registered: " + registerRequestDto.email());
       }
       if (userRepository.findByPhone(registerRequestDto.phone()).isPresent()) {
           throw new DuplicateUserException("Phone already registered: " + registerRequestDto.phone());
       }

       User user = new User(
               registerRequestDto.username(),
               registerRequestDto.email(),
               registerRequestDto.phone(),
               registerRequestDto.legalFirstName(),
               registerRequestDto.legalLastName(),
               registerRequestDto.role(),
               passwordEncoder.encode(registerRequestDto.password())
       );

       User registeredUser = userRepository.save(user);
       String token = jwtService.generateToken(registeredUser);

       return new AuthenticationResponseDto(token);
   }

}
