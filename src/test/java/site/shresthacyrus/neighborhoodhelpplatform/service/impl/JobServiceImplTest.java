package site.shresthacyrus.neighborhoodhelpplatform.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import site.shresthacyrus.neighborhoodhelpplatform.auth.util.AuthUtils;
import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobUpdateRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobDetailResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.mapper.JobMapper;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.JobRepository;
import site.shresthacyrus.neighborhoodhelpplatform.repository.SkillRepository;
import site.shresthacyrus.neighborhoodhelpplatform.util.JobIdGeneratorUtil;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private JobMapper jobMapper;

    @Mock
    private JobIdGeneratorUtil jobIdGeneratorUtil;

    @InjectMocks
    private JobServiceImpl jobService;

    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("seekerUser");
    }

    @Test
    void shouldCreateJobSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(mockUser);

            // Arrange
            JobRequestDto requestDto = new JobRequestDto(
                    "Fix Plumbing",
                    "Need kitchen sink fixed",
                    "12345",
                    50.00,
                    100.00,
                    1L
            );

            Job mappedJob = new Job();
            Skill skill = new Skill("Plumbing");
            skill.setId(1L);

            when(jobRepository.findByTitleIgnoreCaseAndSeekerId("Fix Plumbing", mockUser.getId())).thenReturn(Optional.empty());
            when(jobMapper.jobRequestDtoToJob(requestDto)).thenReturn(mappedJob);
            when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
            when(jobIdGeneratorUtil.generateNextJobPublicId()).thenReturn("JOB-2025-0001");
            when(jobRepository.save(mappedJob)).thenReturn(mappedJob);
            when(jobMapper.jobToJobResponseDto(mappedJob)).thenReturn(new JobResponseDto("JOB-2025-0001","Fix Plumbing","Need kitchen sink fixed","12345",50.00,100.00, JobStatusEnum.OPEN,"Plumbing" ,null,null));

            // Act
            JobResponseDto result = jobService.createJob(requestDto);

            // Assert
            assertThat(result.title()).isEqualTo("Fix Plumbing");
            assertThat(result.zipCode()).isEqualTo("12345");
            assertThat(result.status()).isEqualTo(JobStatusEnum.OPEN);
            assertThat(result.publicId()).isEqualTo("JOB-2025-0001");

            verify(jobRepository).save(mappedJob);
        }
    }

    @Test
    void shouldFindJobByPublicIdSuccessfully() {
        Job job = new Job();
        job.setPublicId("JOB-2025-0001");

        when(jobRepository.findJobByPublicId("JOB-2025-0001")).thenReturn(Optional.of(job));
        when(jobMapper.jobToJobResponseDto(job)).thenReturn(
                new JobResponseDto("JOB-2025-0001", "Need kitchen sink fixed", "Fix Plumbing", "12345", 50.00, 100.00, JobStatusEnum.OPEN, "Plumbing", null, null)
        );

        JobResponseDto result = jobService.findJobByPublicId("JOB-2025-0001");

        assertThat(result.publicId()).isEqualTo("JOB-2025-0001");
        verify(jobRepository).findJobByPublicId("JOB-2025-0001");
    }

    @Test
    void shouldThrowWhenJobNotFoundByPublicId() {
        when(jobRepository.findJobByPublicId("invalid-id")).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                site.shresthacyrus.neighborhoodhelpplatform.exception.job.JobNotFoundException.class,
                () -> jobService.findJobByPublicId("invalid-id")
        );
    }

    @Test
    void shouldDeleteJobSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(mockUser);

            Job job = new Job();
            job.setPublicId("JOB-2025-0002");
            job.setSeeker(mockUser);

            when(jobRepository.findJobByPublicId("JOB-2025-0002")).thenReturn(Optional.of(job));

            jobService.deleteJob("JOB-2025-0002");

            verify(jobRepository).delete(job);
        }
    }

    @Test
    void shouldThrowWhenUnauthorizedToDeleteJob() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            User anotherUser = new User();
            anotherUser.setId(2L); // Not the owner
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(anotherUser);

            Job job = new Job();
            job.setPublicId("JOB-2025-0002");
            job.setSeeker(mockUser); // different owner

            when(jobRepository.findJobByPublicId("JOB-2025-0002")).thenReturn(Optional.of(job));

            org.junit.jupiter.api.Assertions.assertThrows(
                    org.springframework.security.access.AccessDeniedException.class,
                    () -> jobService.deleteJob("JOB-2025-0002")
            );

            // 👇 FIXED THIS LINE
            verify(jobRepository, never()).delete(any(Job.class));
        }
    }

    @Test
    void shouldUpdateJobSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(mockUser);

            // Arrange
            Job existingJob = new Job();
            existingJob.setId(1L);
            existingJob.setPublicId("JOB-2025-0003");
            existingJob.setSeeker(mockUser);
            existingJob.setMinPrice(BigDecimal.valueOf(50));
            existingJob.setMaxPrice(BigDecimal.valueOf(100));

            Skill newSkill = new Skill("Electrical");
            newSkill.setId(2L);

            JobUpdateRequestDto updateDto = new JobUpdateRequestDto(
                    "Fix Light",
                    "Need ceiling light fixed",
                    "54321",
                    BigDecimal.valueOf(60),
                    BigDecimal.valueOf(120),
                    2L
            );

            when(jobRepository.findJobByPublicId("JOB-2025-0003")).thenReturn(Optional.of(existingJob));
            when(skillRepository.findById(2L)).thenReturn(Optional.of(newSkill));
            when(jobRepository.save(existingJob)).thenReturn(existingJob);
            when(jobMapper.jobToJobResponseDto(existingJob)).thenReturn(
                    new JobResponseDto("JOB-2025-0003","Fix Light", "Need ceiling light fixed", "54321", 60.0, 120.0, JobStatusEnum.OPEN, "Electrical", null, null)
            );

            // Act
            JobResponseDto result = jobService.updateJob("JOB-2025-0003", updateDto);

            // Assert
            assertThat(result.title()).isEqualTo("Fix Light");
            assertThat(result.zipCode()).isEqualTo("54321");
            assertThat(result.minPrice()).isEqualTo(60.0);
            assertThat(result.maxPrice()).isEqualTo(120.0);
            assertThat(result.skillName()).isEqualTo("Electrical");

            verify(jobRepository).save(existingJob);
        }
    }
    @Test
    void shouldCompleteJobSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(mockUser);

            // Arrange
            Job job = new Job();
            job.setPublicId("JOB-2025-0004");
            job.setStatus(JobStatusEnum.IN_PROGRESS);
            job.setSeeker(mockUser);

            when(jobRepository.findJobByPublicId("JOB-2025-0004")).thenReturn(Optional.of(job));
            when(jobRepository.save(job)).thenReturn(job);
            when(jobMapper.jobToJobDetailResponseDto(job)).thenReturn(
                    new JobDetailResponseDto("JOB-2025-0004", "Complete Plumbing", "Plumbing", "12345",new BigDecimal("60.0"), new BigDecimal("120.0"), "COMPLETED", null, null, null));

            // Act
            JobDetailResponseDto result = jobService.completeJob("JOB-2025-0004");

            // Assert
            assertThat(result.status()).isEqualTo("COMPLETED");
            assertThat(result.publicId()).isEqualTo("JOB-2025-0004");

            verify(jobRepository).save(job);
        }
    }

    @Test
    void shouldCancelJobSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(mockUser);

            // Arrange
            Job job = new Job();
            job.setPublicId("JOB-2025-0005");
            job.setStatus(JobStatusEnum.IN_PROGRESS);
            job.setSeeker(mockUser);

            when(jobRepository.findJobByPublicId("JOB-2025-0005")).thenReturn(Optional.of(job));
            when(jobRepository.save(job)).thenReturn(job);
            when(jobMapper.jobToJobDetailResponseDto(job)).thenReturn(
                    new JobDetailResponseDto("JOB-2025-0005", "Cancel Electrical Job", "Electrical", "12345", new BigDecimal("60.0"), new BigDecimal("120.0"), "CANCELED", null, null, null)
            );

            // Act
            JobDetailResponseDto result = jobService.cancelJob("JOB-2025-0005");

            // Assert
            assertThat(result.status()).isEqualTo("CANCELED");
            assertThat(result.publicId()).isEqualTo("JOB-2025-0005");

            verify(jobRepository).save(job);
        }
    }



}