package site.shresthacyrus.neighborhoodhelpplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;

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

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String zipCode;

    private Double minPrice;
    private Double maxPrice;

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
    private Skill category;

    // Seeker who posted the job
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seeker_id", nullable = false)
    private User seeker;

    // List of bids on this job
    @OneToMany(mappedBy = "job", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Bid> bids;

    // Accepted bid (optional)
    @OneToOne(fetch = FetchType.LAZY)
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


//    public Job(String title, String description, String category, String zipCode,
//               Double minPrice, Double maxPrice, User seeker) {
//        this.title = title;
//        this.description = description;
//        this.category = category;
//        this.zipCode = zipCode;
//        this.minPrice = minPrice;
//        this.maxPrice = maxPrice;
//        this.seeker = seeker;
//        this.status = JobStatusEnum.OPEN;
//    }

}
