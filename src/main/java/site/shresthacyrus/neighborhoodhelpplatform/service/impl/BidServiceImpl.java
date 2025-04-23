package site.shresthacyrus.neighborhoodhelpplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.shresthacyrus.neighborhoodhelpplatform.auth.util.AuthUtils;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid.BidResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.exception.bid.DuplicateBidException;
import site.shresthacyrus.neighborhoodhelpplatform.exception.job.JobNotFoundException;
import site.shresthacyrus.neighborhoodhelpplatform.mapper.BidMapper;
import site.shresthacyrus.neighborhoodhelpplatform.model.Bid;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.BidRepository;
import site.shresthacyrus.neighborhoodhelpplatform.repository.JobRepository;
import site.shresthacyrus.neighborhoodhelpplatform.service.BidService;

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
}
