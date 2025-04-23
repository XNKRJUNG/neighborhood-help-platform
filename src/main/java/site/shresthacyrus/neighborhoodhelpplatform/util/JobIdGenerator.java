package site.shresthacyrus.neighborhoodhelpplatform.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.shresthacyrus.neighborhoodhelpplatform.repository.JobRepository;

import java.time.Year;

@Component
@RequiredArgsConstructor
public class JobIdGenerator {

    private final JobRepository jobRepository;

    public String generateNextJobPublicId() {
        int year = Year.now().getValue();

        // Count how many jobs already exist for this year
        long count = jobRepository.countByYearCreated(year);

        long nextId = count + 1;

        return String.format("JOB-%d-%04d", year, nextId); // e.g., JOB-2025-0007
    }
}
