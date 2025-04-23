package site.shresthacyrus.neighborhoodhelpplatform.service;

import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;

import java.util.List;

public interface JobService {

    // Create a job
    JobResponseDto createJob(JobRequestDto jobRequestDto);

    // Get all jobs
    List<JobResponseDto> findAllJobs();

    // Get a job by id
    JobResponseDto findJobByPublicId(String publicId);

    // Get all jobs by zip code
    List<JobResponseDto> findAllJobsByZipCode(String zipCode);
}
