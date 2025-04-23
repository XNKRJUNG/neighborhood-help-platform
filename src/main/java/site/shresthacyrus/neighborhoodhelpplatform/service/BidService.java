package site.shresthacyrus.neighborhoodhelpplatform.service;

import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid.BidResponseDto;

public interface BidService {

    BidResponseDto createBid(BidRequestDto bidRequestDto, String jobPublicId);
}
