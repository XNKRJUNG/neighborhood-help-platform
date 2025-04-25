package site.shresthacyrus.neighborhoodhelpplatform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidUpdateRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid.BidResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.service.BidService;

@RestController
@RequestMapping("/api/v1/bids")
@RequiredArgsConstructor
public class HelperBidController {

    private final BidService bidService;

    @PreAuthorize("hasRole('HELPER')")
    @GetMapping("/me")
    public ResponseEntity<Page<BidResponseDto>> getMyBids(Pageable pageable) {
        Page<BidResponseDto> myBids = bidService.getMyBids(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(myBids);
    }

    @PreAuthorize("hasRole('HELPER')")
    @PatchMapping("/{bidId}")
    public ResponseEntity<BidResponseDto> updateBid(
            @PathVariable Long bidId,
            @RequestBody BidUpdateRequestDto dto
    ) {
        BidResponseDto updated = bidService.updateBid(bidId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @PreAuthorize("hasRole('HELPER')")
    @DeleteMapping("/{bidId}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long bidId) {
        bidService.deleteBid(bidId);
        return ResponseEntity.noContent().build();
    }
}
