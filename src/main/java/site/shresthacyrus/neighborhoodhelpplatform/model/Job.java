package site.shresthacyrus.neighborhoodhelpplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
@NoArgsConstructor
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "job_id")
    private Long id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private String publicId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal minPrice;
    @Column(precision = 10, scale = 2)
    private BigDecimal maxPrice;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private JobStatusEnum status; // OPEN, ACCEPTED, COMPLETED, CANCELED

    // === Relationships ===

    /*
    *Refactored: category is now a Skill.
    * *Ensure every job category comes from a real, helper-searchable skill,
    * *Allow advanced filtering (e.g., "show jobs I’m skilled in")
    */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    // Seeker who posted the job
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seeker_id", nullable = false)
    private User seeker;

    // List of bids on this job
    @OneToMany(mappedBy = "job", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Bid> bids;

    // Accepted bid (optional)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accepted_bid_id")
    private Bid acceptedBid;

    // Reviews related to this job (optional, usually 2)
    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviews;

    // Photos associated with this job
    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos;

    // === Lifecycle callback ===

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = JobStatusEnum.OPEN;
        }
    }

}
