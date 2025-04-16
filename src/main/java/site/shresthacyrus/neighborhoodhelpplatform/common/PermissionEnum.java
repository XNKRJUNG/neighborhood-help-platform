package site.shresthacyrus.neighborhoodhelpplatform.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionEnum {
    ADMIN_WRITE("admin:write"),
    ADMIN_READ("admin:read"),
    HELPER_WRITE("helper:write"),
    HELPER_READ("helper:read"),
    SEEKER_WRITE("seeker:write"),
    SEEKER_READ("seeker:read");

    private final String permission;
}
