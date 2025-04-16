package site.shresthacyrus.neighborhoodhelpplatform.protectedController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/helper")
public class HelperController {

    @GetMapping
    public String getHelper(){
        return "Helper: secured end point";
    }

    @GetMapping("/admin-write")
    @PreAuthorize("hasAuthority('admin:write')")
    public String memberOnlyForAdminWrite() {
        return "Helper: secured end point only for admin write";
    }

}
