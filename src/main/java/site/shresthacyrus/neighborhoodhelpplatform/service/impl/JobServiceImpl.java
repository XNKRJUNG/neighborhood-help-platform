package site.shresthacyrus.neighborhoodhelpplatform.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.shresthacyrus.neighborhoodhelpplatform.auth.util.AuthUtils;
import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobUpdateRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobDetailResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.exception.job.DuplicateJobTitleException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.job.InvalidJobStatusTransitionException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.job.InvalidPriceRangeException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.job.JobNotFoundException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.skill.SkillNotFoundException;
import site.shresthacyrus.neighborhoodhelpplatform.mapper.JobMapper;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.JobRepository;
import site.shresthacyrus.neighborhoodhelpplatform.repository.SkillRepository;
import site.shresthacyrus.neighborhoodhelpplatform.repository.UserRepository;
import site.shresthacyrus.neighborhoodhelpplatform.service.JobService;
import site.shresthacyrus.neighborhoodhelpplatform.specification.JobSpecifications;
import site.shresthacyrus.neighborhoodhelpplatform.util.JobIdGeneratorUtil;
import site.shresthacyrus.neighborhoodhelpplatform.util.JobStatusTransitionValidator;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final JobIdGeneratorUtil jobIdGeneratorUtil;

    @Transactional
    @Override
    public JobResponseDto createJob(JobRequestDto jobRequestDto) {

        User seeker = AuthUtils.getCurrentUser();

        // Check whether a job already exists
        jobRepository.findByTitleIgnoreCaseAndSeekerId(jobRequestDto.title(), seeker.getId())
                .ifPresent(existingJob -> {
                    throw new DuplicateJobTitleException(
                            "Job with title " + jobRequestDto.title() + " already exists by this seeker: " + seeker.getLegalFullName()
                    );
                });

        Job mappedJob = jobMapper.jobRequestDtoToJob(jobRequestDto);
        Skill existingSkill = skillRepository.findById(jobRequestDto.skillId())
                .orElseThrow(() -> new IllegalArgumentException("Skill with id " + jobRequestDto.skillId() + " does not exist"));

        String customPublicId = jobIdGeneratorUtil.generateNextJobPublicId();
        mappedJob.setPublicId(customPublicId);
        mappedJob.setSeeker(seeker);
        mappedJob.setSkill(existingSkill);

        Job savedJob = jobRepository.save(mappedJob);
        return jobMapper.jobToJobResponseDto(savedJob);
    }

    @Override
    public JobResponseDto findJobByPublicId(String publicId) {
        // Check whether a job exists
        Job existingJob = jobRepository.findJobByPublicId(publicId)
                .orElseThrow(() -> new JobNotFoundException("Job with id " + publicId + " doesn't exists."));

        return jobMapper.jobToJobResponseDto(existingJob);
    }

    @Override
    public JobDetailResponseDto findJobDetailByPublicId(String publicId) {
        Job job = jobRepository.findJobByPublicId(publicId)
                .orElseThrow(() -> new JobNotFoundException("Job not found."));
        return jobMapper.jobToJobDetailResponseDto(job);
    }

    @Override
    public Page<JobResponseDto> getFilteredJobs(Long skillId, String zipCode, String title,
                                                Double minPrice, Double maxPrice, Pageable pageable) {

        Specification<Job> spec = Specification.where(JobSpecifications.hasSkill(skillId))
                .and(JobSpecifications.hasZipCode(zipCode))
                .and(JobSpecifications.hasTitle(title))
                .and(JobSpecifications.hasMinPrice(minPrice))
                .and(JobSpecifications.hasMaxPrice(maxPrice));

        Page<Job> jobPage = jobRepository.findAll(spec, pageable);

        return jobPage.map(jobMapper::jobToJobResponseDto);
    }

    @Override
    @Transactional
    public JobResponseDto updateJob(String publicId, JobUpdateRequestDto jobUpdateRequestDto) {
        User currentUser = AuthUtils.getCurrentUser();

        // Find the job by publicId
        Job job = jobRepository.findJobByPublicId(publicId)
                .orElseThrow(() -> new JobNotFoundException("Job with id " + publicId + " doesn't exist."));

        // Ensure the current user is the job owner (seeker)
        if (!job.getSeeker().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to update this job.");
        }

        // Validate price range
        if (jobUpdateRequestDto.minPrice().compareTo(jobUpdateRequestDto.maxPrice()) > 0){
            throw new InvalidPriceRangeException("Minimum price cannot be greater than maximum price.");
        }

        // Update allowed fields
        job.setTitle(jobUpdateRequestDto.title());
        job.setDescription(jobUpdateRequestDto.description());
        job.setZipCode(jobUpdateRequestDto.zipCode());
        job.setMinPrice(jobUpdateRequestDto.minPrice());
        job.setMaxPrice(jobUpdateRequestDto.maxPrice());

        // Validate skill exists
        Skill skill = skillRepository.findById(jobUpdateRequestDto.skillId())
                .orElseThrow(() -> new SkillNotFoundException("Skill with id " + jobUpdateRequestDto.skillId() + " does not exist"));
        job.setSkill(skill);

        // Save and return DTO
        Job updatedJob = jobRepository.save(job);
        return jobMapper.jobToJobResponseDto(updatedJob);
    }

    @Override
    @Transactional
    public void deleteJob(String publicId) {
        User currentUser = AuthUtils.getCurrentUser();

        // Find job by publicId
        Job job = jobRepository.findJobByPublicId(publicId)
                .orElseThrow(() -> new JobNotFoundException("Job with ID " + publicId + " not found."));

        // Check ownership
        if (!job.getSeeker().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this job.");
        }

        // Delete it
        jobRepository.delete(job);
    }

    @Override
    public Page<JobResponseDto> getJobsBySeekerUsername(String username, Pageable pageable) {
        // Find the user
        User seeker = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found."));

        // Ensure the user is a SEEKER
        if (!seeker.getRole().name().equals("SEEKER")) {
            throw new IllegalArgumentException("User '" + username + "' is not a Seeker.");
        }

        // Get jobs posted by that seeker
        Page<Job> jobs = jobRepository.findAllBySeekerId(seeker.getId(), pageable);

        // Map and return
        return jobs.map(jobMapper::jobToJobResponseDto);
    }

    @Override
    @Transactional
    public JobDetailResponseDto completeJob(String publicId) {
        User currentUser = AuthUtils.getCurrentUser();

        // Find the job
        Job job = jobRepository.findJobByPublicId(publicId)
                .orElseThrow(() -> new JobNotFoundException("Job with ID " + publicId + " not found."));

        // Ensure the current user is the job owner (seeker)
        if (!job.getSeeker().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to complete this job.");
        }

        // Validate that job is eligible to be completed
        if (!JobStatusTransitionValidator.isValidTransition(job.getStatus(), JobStatusEnum.COMPLETED)) {
            throw new InvalidJobStatusTransitionException(
                    "Cannot mark job as COMPLETED from status: " + job.getStatus()
            );
        }

        // Set status to COMPLETED
        job.setStatus(JobStatusEnum.COMPLETED);
        Job updatedJob = jobRepository.save(job);

        // Return mapped response
        return jobMapper.jobToJobDetailResponseDto(updatedJob); // or use JobDetailResponseDto if preferred
    }

    @Override
    @Transactional
    public JobDetailResponseDto cancelJob(String publicId) {
        User currentUser = AuthUtils.getCurrentUser();

        Job job = jobRepository.findJobByPublicId(publicId)
                .orElseThrow(() -> new JobNotFoundException("Job with ID " + publicId + " not found."));

        if (!job.getSeeker().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to cancel this job.");
        }

        if (!JobStatusTransitionValidator.isValidTransition(job.getStatus(), JobStatusEnum.CANCELED)) {
            throw new InvalidJobStatusTransitionException(
                    "Cannot mark job as CANCELED from status: " + job.getStatus()
            );
        }

        job.setStatus(JobStatusEnum.CANCELED);
        Job updatedJob = jobRepository.save(job);

        return jobMapper.jobToJobDetailResponseDto(updatedJob);
    }

    // Depreciated: No filters here
    //    @Override
    //    public List<JobResponseDto> getAllJobs() {
    //        return jobMapper.jobsToJobResponseDtoList(jobRepository.findAll());
    //    }

    // Depreciated: getFilteredJob now implements this as well
    //    @Override
    //    public List<JobResponseDto> findAllJobsByZipCode(String zipCode) {
    //        List<Job> existingJobs = jobRepository.findAllByZipCode(zipCode);
    //
    //        if (existingJobs.isEmpty()) {
    //            throw new JobNotFoundException("No jobs found for zip code " + zipCode);
    //        }
    //
    //        return jobMapper.jobsToJobResponseDtoList(existingJobs);
    //    }

}
