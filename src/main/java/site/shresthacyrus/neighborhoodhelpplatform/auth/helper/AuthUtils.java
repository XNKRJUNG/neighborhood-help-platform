package site.shresthacyrus.neighborhoodhelpplatform.auth.helper;

import org.springframework.security.core.context.SecurityContextHolder;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;

public class AuthUtils {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}