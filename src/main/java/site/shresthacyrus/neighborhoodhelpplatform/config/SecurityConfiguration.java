package site.shresthacyrus.neighborhoodhelpplatform.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.shresthacyrus.neighborhoodhelpplatform.common.PermissionEnum;
import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable) // As JWT is state-less, we can disable CSRF
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/api/v1/auth/*").permitAll()
//                                .requestMatchers("/api/v1/auth/sign-up").permitAll()
//                                .requestMatchers("/api/v1/auth/authenticate").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").permitAll()
                                .requestMatchers("/api/v1/seeker/**").hasAnyRole(RoleEnum.ADMIN.name(),RoleEnum.SEEKER.name())
                                .requestMatchers("/api/v1/helper/**").hasAnyRole(RoleEnum.ADMIN.name(),RoleEnum.HELPER.name())
                                .requestMatchers("/api/v1/admin/**").hasAnyRole(RoleEnum.ADMIN.name())
                                .requestMatchers("/api/v1/helper/admin-write").hasAuthority(
                                        PermissionEnum.ADMIN_WRITE.getPermission()
                                )
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}
