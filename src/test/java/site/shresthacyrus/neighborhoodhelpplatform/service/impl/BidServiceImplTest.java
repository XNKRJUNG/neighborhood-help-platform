package site.shresthacyrus.neighborhoodhelpplatform.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.shresthacyrus.neighborhoodhelpplatform.auth.util.AuthUtils;
import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;
import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.request.bid.BidUpdateRequestDto;
import site.shresthacyrus.neighborhoodhelpplatform.dto.response.bid.BidResponseDto;
import site.shresthacyrus.neighborhoodhelpplatform.mapper.BidMapper;
import site.shresthacyrus.neighborhoodhelpplatform.model.Bid;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;
import site.shresthacyrus.neighborhoodhelpplatform.repository.BidRepository;
import site.shresthacyrus.neighborhoodhelpplatform.repository.JobRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceImplTest {

    @Mock
    private BidMapper bidMapper;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private BidServiceImpl bidService;

    private User helper;
    private Job job;
    private User seeker;

    @BeforeEach
    void setup() {
        helper = new User();
        helper.setId(1L);
        helper.setUsername("helperUser");
        helper.setRole(RoleEnum.HELPER);

        seeker = new User();
        seeker.setId(2L);
        seeker.setUsername("seekerUser");

        job = new Job();
        job.setId(10L);
        job.setPublicId("JOB-2025-0010");
        job.setSkill(new Skill("Plumbing"));
        job.setCreatedAt(LocalDateTime.now());
        job.setSeeker(seeker);  // <- ADD THIS
    }


    @Test
    void shouldCreateBidSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(helper);

            // Arrange
            BidRequestDto requestDto = new BidRequestDto(new BigDecimal("100.0"), "I can fix it fast!");
            Bid mappedBid = new Bid();
            Bid savedBid = new Bid();
            savedBid.setId(1L);

            when(jobRepository.findJobByPublicId("JOB-2025-0010")).thenReturn(Optional.of(job));
            when(bidRepository.findByHelperUsernameIgnoreCaseAndJobPublicId(helper.getUsername(), "JOB-2025-0010")).thenReturn(Optional.empty());
            when(bidMapper.bidRequestDtoToBid(requestDto)).thenReturn(mappedBid);
            when(bidRepository.save(mappedBid)).thenReturn(savedBid);
            when(bidMapper.bidToBidResponseDto(savedBid)).thenReturn(
                    new BidResponseDto(1L, "JOB-2025-0010","helperUser","Helper User", new BigDecimal("100.0"), "I can fix it fast!", LocalDateTime.now(),false)
            );

            // Act
            BidResponseDto result = bidService.createBid(requestDto, "JOB-2025-0010");

            // Assert
            assertThat(result.amount()).isEqualTo(new BigDecimal("100.0"));
            assertThat(result.message()).isEqualTo("I can fix it fast!");
            assertThat(result.accepted()).isFalse();

            verify(bidRepository).save(mappedBid);
        }
    }

    @Test
    void shouldGetMyBidsSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(helper);

            // Arrange
            Bid bid = new Bid();
            bid.setId(1L);
            bid.setAmount(BigDecimal.valueOf(100));
            bid.setCreatedAt(LocalDateTime.now());

            Page<Bid> page = new PageImpl<>(List.of(bid));
            when(bidRepository.findAllByHelperId(helper.getId(), Pageable.unpaged())).thenReturn(page);
            when(bidMapper.bidToBidResponseDto(any())).thenReturn(
                    new BidResponseDto(1L, "JOB-2025-0010","helerUser","Helper User", new BigDecimal(100.0), "Bid message", LocalDateTime.now(),false)
            );

            // Act
            Page<BidResponseDto> result = bidService.getMyBids(Pageable.unpaged());

            // Assert
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).amount()).isEqualTo(new BigDecimal("100"));
            assertThat(result.getContent().get(0).accepted()).isFalse();

            verify(bidRepository).findAllByHelperId(helper.getId(), Pageable.unpaged());
        }
    }
    @Test
    void shouldGetBidByIdSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(helper);

            // Arrange
            Bid bid = new Bid();
            bid.setId(1L);
            bid.setJob(job);
            bid.setHelper(helper);
            bid.setAmount(new BigDecimal("100.0"));

            when(bidRepository.findById(1L)).thenReturn(Optional.of(bid));
            when(bidMapper.bidToBidResponseDto(bid)).thenReturn(
                    new BidResponseDto(1L, "JOB-2025-0010", "helperUser", "Helper User", new BigDecimal("100.0"), "Bid message", LocalDateTime.now(), false)
            );

            // Act
            BidResponseDto result = bidService.getBidById(1L);

            // Assert
            assertThat(result.amount()).isEqualTo(new BigDecimal("100.0"));
            assertThat(result.helperUsername()).isEqualTo("helperUser");

            verify(bidRepository).findById(1L);
        }
    }

    @Test
    void shouldUpdateBidSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(helper);

            // Arrange
            Bid bid = new Bid();
            bid.setId(1L);
            bid.setJob(job);
            bid.setHelper(helper);
            bid.setAccepted(false);

            when(bidRepository.findById(1L)).thenReturn(Optional.of(bid));
            when(bidRepository.save(any(Bid.class))).thenReturn(bid);
            when(bidMapper.bidToBidResponseDto(bid)).thenReturn(
                    new BidResponseDto(1L, "JOB-2025-0010", "helperUser", "Helper User", new BigDecimal("150.0"), "Updated bid", LocalDateTime.now(), false)
            );

            BidUpdateRequestDto updateDto = new BidUpdateRequestDto(new BigDecimal("150.0"), "Updated bid");

            // Act
            BidResponseDto result = bidService.updateBid(1L, updateDto);

            // Assert
            assertThat(result.amount()).isEqualTo(new BigDecimal("150.0"));
            assertThat(result.message()).isEqualTo("Updated bid");

            verify(bidRepository).save(bid);
        }
    }

    @Test
    void shouldDeleteBidSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(helper);

            // Arrange
            Bid bid = new Bid();
            bid.setId(1L);
            bid.setHelper(helper);
            bid.setAccepted(false);

            when(bidRepository.findById(1L)).thenReturn(Optional.of(bid));

            // Act
            bidService.deleteBid(1L);

            // Assert
            verify(bidRepository).delete(bid);
        }
    }

    @Test
    void shouldAcceptBidSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(seeker);

            Bid bid = new Bid();
            bid.setId(1L);
            bid.setJob(job);
            bid.setHelper(helper);

            job.setStatus(JobStatusEnum.OPEN);

            when(jobRepository.findJobByPublicId(job.getPublicId())).thenReturn(Optional.of(job));
            when(bidRepository.findById(1L)).thenReturn(Optional.of(bid));
            when(bidRepository.findByJobIdAndAcceptedTrue(job.getId())).thenReturn(Optional.empty());
            when(bidRepository.save(bid)).thenReturn(bid);
            when(bidMapper.bidToBidResponseDto(bid)).thenReturn(new BidResponseDto(
                    1L, job.getPublicId(), helper.getUsername(), helper.getLegalFullName(), bid.getAmount(), bid.getMessage(), bid.getCreatedAt(), true
            ));

            BidResponseDto result = bidService.acceptBid(job.getPublicId(), bid.getId());

            assertThat(result.accepted()).isTrue();
            verify(bidRepository).save(bid);
            verify(jobRepository).save(job);
        }
    }


    @Test
    void shouldGetBidsForJobSuccessfully() {
        try (MockedStatic<AuthUtils> mockedAuth = mockStatic(AuthUtils.class)) {
            mockedAuth.when(AuthUtils::getCurrentUser).thenReturn(job.getSeeker());

            // Arrange
            Bid bid = new Bid();
            bid.setId(1L);
            bid.setAmount(new BigDecimal("120.0"));
            bid.setHelper(helper);

            job.setBids(List.of(bid));

            when(jobRepository.findJobByPublicId("JOB-2025-0010")).thenReturn(Optional.of(job));
            when(bidMapper.bidsToBidResponseDtoList(job.getBids())).thenReturn(List.of(
                    new BidResponseDto(1L, "JOB-2025-0010", "helperUser", "Helper User", new BigDecimal("120.0"), "Bid message", LocalDateTime.now(), false)
            ));

            // Act
            List<BidResponseDto> result = bidService.getBidsForJob("JOB-2025-0010");

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).amount()).isEqualTo(new BigDecimal("120.0"));

            verify(jobRepository).findJobByPublicId("JOB-2025-0010");
        }
    }
}
