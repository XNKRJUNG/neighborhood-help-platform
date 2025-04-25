package site.shresthacyrus.neighborhoodhelpplatform.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import site.shresthacyrus.neighborhoodhelpplatform.auth.util.AuthUtils;
import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid.BidResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.exception.bid.BidAlreadyAcceptedException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.bid.BidNotFoundException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.bid.DuplicateBidException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.job.InvalidJobStatusTransitionException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.job.JobNotFoundException;
import site.shresthacyrus.neighborhoodhelpplatform.mapper.BidMapper;
import site.shresthacyrus.neighborhoodhelpplatform.model.Bid;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.BidRepository;
import site.shresthacyrus.neighborhoodhelpplatform.repository.JobRepository;
import site.shresthacyrus.neighborhoodhelpplatform.service.BidService;
import site.shresthacyrus.neighborhoodhelpplatform.util.JobStatusTransitionValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    public final BidMapper bidMapper;
    public final BidRepository bidRepository;
    public final JobRepository jobRepository;

    @Override
    public BidResponseDto createBid(BidRequestDto bidRequestDto, String jobPublicId) {
    User helper = AuthUtils.getCurrentUser();
        // Check whether a bid already exists from this helper
        Job fetchJob =jobRepository.findJobByPublicId(jobPublicId).orElseThrow(() -> new JobNotFoundException("Job with public id " + jobPublicId + " does not exist"));
        bidRepository.findByHelperUsernameIgnoreCaseAndJobPublicId(helper.getUsername(), jobPublicId)
                .ifPresent(existingBid -> {
                    throw new DuplicateBidException("You have already placed bid on this job.");
                });
        Bid mappedBid = bidMapper.bidRequestDtoToBid(bidRequestDto);

        mappedBid.setHelper(helper);
        mappedBid.setJob(fetchJob);

        Bid savedBid = bidRepository.save(mappedBid);
        return bidMapper.bidToBidResponseDto(savedBid);
    }

    @Override
    public List<BidResponseDto> getBidsForJob(String jobPublicId) {
        User currentUser = AuthUtils.getCurrentUser();

        // Find the job by publicId
        Job job = jobRepository.findJobByPublicId(jobPublicId)
                .orElseThrow(() -> new JobNotFoundException("Job with ID " + jobPublicId + " not found."));

        // Check that the current user is the owner (Seeker)
        if (!job.getSeeker().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to view bids for this job.");
        }

        // Get bids for this job
        List<Bid> bids = job.getBids(); // assuming Bid entity has job relationship mapped

        // Convert to DTO
        return bidMapper.bidsToBidResponseDtoList(bids);
    }

    @Override
    public Page<BidResponseDto> getMyBids(Pageable pageable) {
        User currentUser = AuthUtils.getCurrentUser();

        // Ensure the user is a HELPER
        if (!currentUser.getRole().name().equals("HELPER")) {
            throw new AccessDeniedException("Only helpers can view their own bids.");
        }

        // Fetch bids placed by this user
        Page<Bid> bids = bidRepository.findAllByHelperId(currentUser.getId(), pageable);

        // Map to DTO
        return bids.map(bidMapper::bidToBidResponseDto);
    }

    @Override
    @Transactional
    public BidResponseDto acceptBid(String jobPublicId, Long bidId) {
        User currentUser = AuthUtils.getCurrentUser();

        // Find the job
        Job job = jobRepository.findJobByPublicId(jobPublicId)
                .orElseThrow(() -> new JobNotFoundException("Job with ID " + jobPublicId + " not found."));

        // Verify the current user is the seeker who owns this job
        if (!job.getSeeker().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to accept a bid for this job.");
        }

        // Find the bid
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new BidNotFoundException("Bid with ID " + bidId + " not found."));

        // Ensure the bid actually belongs to the job
        if (!bid.getJob().getId().equals(job.getId())) {
            throw new IllegalArgumentException("Bid does not belong to the specified job.");
        }

        // Check if this job already has an accepted bid
        Optional<Bid> alreadyAcceptedBid = bidRepository.findByJobIdAndAcceptedTrue(job.getId());

        if (alreadyAcceptedBid.isPresent()) {
            throw new BidAlreadyAcceptedException("A bid has already been accepted for this job.");
        }

        // Accept the bid
        bid.setAccepted(true);
        job.setAcceptedBid(bid);
        if (!JobStatusTransitionValidator.isValidTransition(job.getStatus(), JobStatusEnum.IN_PROGRESS)) {
            throw new InvalidJobStatusTransitionException("Cannot mark job as IN_PROGRESS from status: " + job.getStatus());
        }
        job.setStatus(JobStatusEnum.IN_PROGRESS);

        // Save and return
        Bid saved = bidRepository.save(bid);
        jobRepository.save(job);
        return bidMapper.bidToBidResponseDto(saved);
    }

}
