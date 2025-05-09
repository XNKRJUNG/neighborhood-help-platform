package site.shresthacyrus.neighborhoodhelpplatform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.job.JobDetailResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    Optional<Job> findJobByPublicId(String publicId);

    Optional<Job> findByTitleIgnoreCaseAndSeekerId(String title, Long seekerId);

    JobDetailResponseDto findJobDetailByPublicId(String publicId);

    Page<Job> findAllBySeekerId(Long seekerId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM jobs WHERE EXTRACT(YEAR FROM created_at) = :year", nativeQuery = true)
    long countByYearCreated(@Param("year") int year);

    // ⚠️ You do NOT need to define this manually:
    // Page<Job> findAll(Specification<Job> spec, Pageable pageable);
    // It comes automatically from JpaSpecificationExecutor
    // List<Job> findAllByZipCode(String zipCode);
}