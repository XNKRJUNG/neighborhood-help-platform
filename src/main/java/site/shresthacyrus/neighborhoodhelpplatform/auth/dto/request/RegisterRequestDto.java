package site.shresthacyrus.neighborhoodhelpplatform.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;

public record RegisterRequestDto(

        @NotBlank(message = "Username cannot be blank/empty/null")
        String username,

        @NotBlank(message = "Email cannot be blank/empty/null")
        @Email(message = "Invalid email format.")
        String email,

        @NotBlank(message = "Phone cannot be blank/empty/null")
        String phone,

        @NotBlank(message = "First name cannot be blank/empty/null")
        String legalFirstName,

        @NotBlank(message = "Last name cannot be blank/empty/null")
        String legalLastName,

        @NotNull(message = "Role must not be null")
        RoleEnum role, // SEEKER or HELPER or ADMIN

        @NotBlank(message = "Password cannot be blank/empty/null")
        String password

) {
}
