package site.shresthacyrus.neighborhoodhelpplatform.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidUpdateRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid.BidResponseDto;

import java.util.List;

public interface BidService {

    BidResponseDto createBid(BidRequestDto bidRequestDto, String jobPublicId);

    List<BidResponseDto> getBidsForJob(String jobPublicId);

    Page<BidResponseDto> getMyBids(Pageable pageable);

    BidResponseDto updateBid(Long bidId, BidUpdateRequestDto dto);

    void deleteBid(Long bidId);

    BidResponseDto acceptBid(String jobPublicId, Long bidId);

}
