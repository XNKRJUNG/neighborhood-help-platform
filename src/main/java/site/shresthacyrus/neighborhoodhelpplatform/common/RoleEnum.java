package site.shresthacyrus.neighborhoodhelpplatform.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    SEEKER(
            Set.of(
                    PermissionEnum.SEEKER_WRITE,
                    PermissionEnum.SEEKER_READ
            )
    ),
    HELPER(
            Set.of(
                    PermissionEnum.HELPER_WRITE,
                    PermissionEnum.HELPER_READ
            )
    ),
    ADMIN(
            Set.of(
                    PermissionEnum.ADMIN_WRITE,
                    PermissionEnum.ADMIN_READ
                    )
    );

    private final Set<PermissionEnum> permissions;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+name()));
        return authorities;
    }
}
