package site.shresthacyrus.neighborhoodhelpplatform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid.BidResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.service.BidService;

@RestController
@RequestMapping("/api/v1/jobs/{publicId}/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    // Create a Bid (HELPER only)
    @PostMapping
    public ResponseEntity<BidResponseDto> createBid(@PathVariable("publicId") String publicId, @RequestBody BidRequestDto bidRequestDto) {
        BidResponseDto createdBid = bidService.createBid(bidRequestDto, publicId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBid);
    }
}
