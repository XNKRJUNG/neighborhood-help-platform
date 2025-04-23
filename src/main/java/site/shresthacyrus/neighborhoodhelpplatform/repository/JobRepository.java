package site.shresthacyrus.neighborhoodhelpplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findJobByPublicId(String publicId);
    Optional<Job> findByTitleIgnoreCaseAndSeekerId (String title, Long seekerId);

    @Query(value = "SELECT COUNT(*) FROM jobs WHERE EXTRACT(YEAR FROM created_at) = :year", nativeQuery = true)
    long countByYearCreated(@Param("year") int year);


}
