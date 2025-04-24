package site.shresthacyrus.neighborhoodhelpplatform.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobUpdateRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobResponseDto;

public interface JobService {

    // Create a job
    JobResponseDto createJob(JobRequestDto jobRequestDto);

    // Get all jobs with a filter
    Page<JobResponseDto> getFilteredJobs(Long skillId, String zipCode, String title,
                                         Double minPrice, Double maxPrice, Pageable pageable);

    // Get a job by id
    JobResponseDto findJobByPublicId(String publicId);

    JobResponseDto updateJob(String publicId, JobUpdateRequestDto jobUpdateRequestDto);

    void deleteJob(String publicId);

    // List<JobResponseDto> getAllJobs();

    // Get all jobs by zip code
    // List<JobResponseDto> findAllJobsByZipCode(String zipCode);

}
