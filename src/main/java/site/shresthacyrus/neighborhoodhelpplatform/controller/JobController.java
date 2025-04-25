package site.shresthacyrus.neighborhoodhelpplatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.job.JobUpdateRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobDetailResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.service.JobService;

import java.util.List;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // Unified endpoint with dynamic filtering + pagination
    @GetMapping
    public ResponseEntity<Page<JobResponseDto>> getFilteredJobs(
            @RequestParam(required = false) Long skillId,
            @RequestParam(required = false) String zipCode,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable
    ) {
        Page<JobResponseDto> jobs = jobService.getFilteredJobs(skillId, zipCode, title, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(jobs);
    }

    // Retrieve a single job by publicId
    @GetMapping("/{publicId}")
    public ResponseEntity<JobDetailResponseDto> findJobByPublicId(@PathVariable String publicId) {
        JobDetailResponseDto job = jobService.findJobDetailByPublicId(publicId);
        return ResponseEntity.ok(job);
    }

    // Create job (SEEKER only)
    @PreAuthorize("hasRole('SEEKER')")
    @PostMapping
    public ResponseEntity<JobResponseDto> createJob(@Valid @RequestBody JobRequestDto jobRequestDto){
        JobResponseDto createdJob = jobService.createJob(jobRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @PreAuthorize("hasRole('SEEKER')")
    @PutMapping("/{publicId}")
    public ResponseEntity<JobResponseDto> updateJob(
            @PathVariable("publicId") String publicId,
            @Valid @RequestBody JobUpdateRequestDto jobUpdateRequestDto
    ) {
        JobResponseDto updatedJob = jobService.updateJob(publicId, jobUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedJob);
    }

    @PreAuthorize("hasRole('SEEKER')")
    @DeleteMapping("/{publicId}")
    public ResponseEntity<Void> deleteJob(@PathVariable("publicId") String publicId) {
        jobService.deleteJob(publicId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
    }

    @GetMapping("/seeker/{username}")
    public ResponseEntity<Page<JobResponseDto>> getJobsBySeeker(
            @PathVariable("username") String username,
            Pageable pageable
    ) {
        Page<JobResponseDto> jobs = jobService.getJobsBySeekerUsername(username, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }

    @PreAuthorize("hasRole('SEEKER')")
    @PostMapping("/{publicId}/complete")
    public ResponseEntity<JobDetailResponseDto> completeJob(@PathVariable("publicId") String publicId) {
        JobDetailResponseDto completed = jobService.completeJob(publicId);
        return ResponseEntity.status(HttpStatus.OK).body(completed);
    }

    @PreAuthorize("hasRole('SEEKER')")
    @PostMapping("/{publicId}/cancel")
    public ResponseEntity<JobDetailResponseDto> cancelJob(@PathVariable String publicId) {
        JobDetailResponseDto canceled = jobService.cancelJob(publicId);
        return ResponseEntity.status(HttpStatus.OK).body(canceled);
    }
}
