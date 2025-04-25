package site.shresthacyrus.neighborhoodhelpplatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid.BidResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.service.BidService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs/{jobPublicId}/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    // Create a Bid (HELPER only)
    @PostMapping
    public ResponseEntity<BidResponseDto> createBid(
            @PathVariable("jobPublicId") String jobPublicId,
            @Valid @RequestBody BidRequestDto bidRequestDto
    ) {
        BidResponseDto createdBid = bidService.createBid(bidRequestDto, jobPublicId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBid);
    }

    @PreAuthorize("hasRole('SEEKER')")
    @GetMapping
    public ResponseEntity<List<BidResponseDto>> getBidsForJob(
            @PathVariable("jobPublicId") String jobPublicId
    ) {
        List<BidResponseDto> bids = bidService.getBidsForJob(jobPublicId);
        return ResponseEntity.status(HttpStatus.OK).body(bids);
    }

    @PreAuthorize("hasRole('SEEKER')")
    @PostMapping("/{bidId}/accept")
    public ResponseEntity<BidResponseDto> acceptBid(
            @PathVariable("jobPublicId") String jobPublicId,
            @PathVariable("bidId") Long bidId
    ) {
        BidResponseDto accepted = bidService.acceptBid(jobPublicId, bidId);
        return ResponseEntity.status(HttpStatus.OK).body(accepted);
    }

}
