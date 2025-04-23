package site.shresthacyrus.neighborhoodhelpplatform.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.shresthacyrus.neighborhoodhelpplatform.auth.util.AuthUtils;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.exception.job.DuplicateJobTitleException;
import site.shresthacyrus.neighborhoodhelpplatform.mapper.JobMapper;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.JobRepository;
import site.shresthacyrus.neighborhoodhelpplatform.repository.SkillRepository;
import site.shresthacyrus.neighborhoodhelpplatform.service.JobService;
import site.shresthacyrus.neighborhoodhelpplatform.util.JobIdGenerator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final JobIdGenerator jobIdGenerator;

    @Transactional
    @Override
    public JobResponseDto createJob(JobRequestDto jobRequestDto) {

        User seeker = AuthUtils.getCurrentUser();

        // Check whether job already exists
        jobRepository.findByTitleIgnoreCaseAndSeekerId(jobRequestDto.title(), seeker.getId())
                .ifPresent(existingJob -> {
                    throw new DuplicateJobTitleException(
                            "Job with title " + jobRequestDto.title() + " already exists by this seeker: " + seeker.getLegalFullName()
                    );
                });

        Job mappedJob = jobMapper.jobRequestDtoToJob(jobRequestDto);
        Skill existingSkill = skillRepository.findById(jobRequestDto.skillId())
                .orElseThrow(() -> new IllegalArgumentException("Skill with id " + jobRequestDto.skillId() + " does not exist"));

        String customPublicId = jobIdGenerator.generateNextJobPublicId();
        mappedJob.setPublicId(customPublicId);
        mappedJob.setSeeker(seeker);
        mappedJob.setSkill(existingSkill);

        Job savedJob = jobRepository.save(mappedJob);
        return jobMapper.jobToJobResponseDto(savedJob);
    }

    @Override
    public List<JobResponseDto> findAllJobs() {
        return List.of();
    }

    @Override
    public JobResponseDto findJobByPublicId(String publicId) {
        return null;
    }

    @Override
    public List<JobResponseDto> findAllJobsByZipCode(String zipCode) {
        return List.of();
    }
}
