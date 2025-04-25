package site.shresthacyrus.neighborhoodhelpplatform.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobUpdateRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobDetailResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobResponseDto;

import java.util.List;

public interface JobService {

    // Create a job
    JobResponseDto createJob(JobRequestDto jobRequestDto);

    // Get all jobs with a filter
    Page<JobResponseDto> getFilteredJobs(Long skillId, String zipCode, String title, Double minPrice, Double maxPrice, JobStatusEnum status, Pageable pageable);

    // Get a job by id
    JobResponseDto findJobByPublicId(String publicId);

    JobDetailResponseDto findJobDetailByPublicId(String publicId);

    JobResponseDto updateJob(String publicId, JobUpdateRequestDto jobUpdateRequestDto);

    void deleteJob(String publicId);

    Page<JobResponseDto> getJobsBySeekerUsername(String username, Pageable pageable);

    JobDetailResponseDto completeJob(String publicId);

    JobDetailResponseDto cancelJob(String publicId);

    // List<JobResponseDto> getAllJobs();

    // Get all jobs by zip code
    // List<JobResponseDto> findAllJobsByZipCode(String zipCode);

}
