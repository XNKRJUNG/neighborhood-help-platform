package site.shresthacyrus.neighborhoodhelpplatform.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;
import site.shresthacyrus.neighborhoodhelpplatform.model.Bid;
import site.shresthacyrus.neighborhoodhelpplatform.model.Job;
import site.shresthacyrus.neighborhoodhelpplatform.model.Skill;
import site.shresthacyrus.neighborhoodhelpplatform.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BidRepositoryTest {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;

    private Skill createSkill() {
        Skill skill = new Skill("Plumbing");
        return skillRepository.save(skill);
    }

    private User createSeeker() {
        User seeker = new User(
                "seekerUser",
                "seeker@example.com",
                "1112223333",
                "Seeker",
                "User",
                RoleEnum.SEEKER,
                "seekerPassword"
        );
        return userRepository.save(seeker);
    }

    private User createHelper() {
        User helper = new User(
                "helperUser",
                "helper@example.com",
                "4445556666",
                "Helper",
                "User",
                RoleEnum.HELPER,
                "helperPassword"
        );
        return userRepository.save(helper);
    }

    private Job createJob(Skill skill, User seeker) {
        Job job = new Job();
        job.setPublicId(UUID.randomUUID().toString());
        job.setTitle("Fix Plumbing Leak");
        job.setDescription("Fix leaking kitchen sink");
        job.setZipCode("12345");
        job.setMinPrice(BigDecimal.valueOf(50));
        job.setMaxPrice(BigDecimal.valueOf(150));
        job.setCreatedAt(LocalDateTime.now());
        job.setSkill(skill);
        job.setSeeker(seeker);
        return jobRepository.save(job);
    }

    private Bid createBid(Job job, User helper, BigDecimal amount, boolean accepted) {
        Bid bid = new Bid();
        bid.setAmount(amount);
        bid.setMessage("I can fix this!");
        bid.setAccepted(accepted);
        bid.setCreatedAt(LocalDateTime.now());
        bid.setJob(job);
        bid.setHelper(helper);
        return bidRepository.save(bid);
    }

    @Test
    @DisplayName("Should save and find Bid by helper username and job public ID")
    void shouldFindBidByHelperUsernameAndJobPublicId() {
        Skill skill = createSkill();
        User seeker = createSeeker();
        User helper = createHelper();
        Job job = createJob(skill, seeker);
        Bid bid = createBid(job, helper, BigDecimal.valueOf(100), false);

        Optional<Bid> foundBid = bidRepository.findByHelperUsernameIgnoreCaseAndJobPublicId(helper.getUsername(), job.getPublicId());

        assertThat(foundBid).isPresent();
        assertThat(foundBid.get().getId()).isEqualTo(bid.getId());
    }

    @Test
    @DisplayName("Should find all Bids by helper ID")
    void shouldFindAllBidsByHelperId() {
        Skill skill = createSkill();
        User seeker = createSeeker();
        User helper = createHelper();
        Job job1 = createJob(skill, seeker);
        Job job2 = createJob(skill, seeker);

        createBid(job1, helper, BigDecimal.valueOf(80), false);
        createBid(job2, helper, BigDecimal.valueOf(120), false);

        Page<Bid> bidPage = bidRepository.findAllByHelperId(helper.getId(), PageRequest.of(0, 10));

        assertThat(bidPage.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("Should find accepted Bid by job ID")
    void shouldFindAcceptedBidByJobId() {
        Skill skill = createSkill();
        User seeker = createSeeker();
        User helper = createHelper();
        Job job = createJob(skill, seeker);

        createBid(job, helper, BigDecimal.valueOf(95), true);

        Optional<Bid> acceptedBid = bidRepository.findByJobIdAndAcceptedTrue(job.getId());

        assertThat(acceptedBid).isPresent();
        assertThat(acceptedBid.get().getAccepted()).isTrue();
    }
}