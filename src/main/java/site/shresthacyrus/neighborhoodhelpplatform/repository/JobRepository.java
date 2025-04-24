package site.shresthacyrus.neighborhoodhelpplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    Optional<Job> findJobByPublicId(String publicId);

    Optional<Job> findByTitleIgnoreCaseAndSeekerId(String title, Long seekerId);

    List<Job> findAllByZipCode(String zipCode);

    @Query(value = "SELECT COUNT(*) FROM jobs WHERE EXTRACT(YEAR FROM created_at) = :year", nativeQuery = true)
    long countByYearCreated(@Param("year") int year);

    // ⚠️ You do NOT need to define this manually:
    // Page<Job> findAll(Specification<Job> spec, Pageable pageable);
    // It comes automatically from JpaSpecificationExecutor
}