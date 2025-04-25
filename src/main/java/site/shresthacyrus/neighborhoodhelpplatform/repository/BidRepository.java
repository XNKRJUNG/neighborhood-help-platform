package site.shresthacyrus.neighborhoodhelpplatform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.shresthacyrus.neighborhoodhelpplatform.model.Bid;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    Optional<Bid> findByHelperUsernameIgnoreCaseAndJobPublicId(String helperUsername, String jobPublicId);

    Page<Bid> findAllByHelperId(Long helperId, Pageable pageable);

    Optional<Bid> findByJobIdAndAcceptedTrue(Long jobId);
}
