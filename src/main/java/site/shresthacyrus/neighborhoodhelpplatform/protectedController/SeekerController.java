package site.shresthacyrus.neighborhoodhelpplatform.protectedController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seeker")
public class SeekerController {

    @GetMapping
    public String getSeeker(){
        return "Seeker: secured end point";
    }

}
