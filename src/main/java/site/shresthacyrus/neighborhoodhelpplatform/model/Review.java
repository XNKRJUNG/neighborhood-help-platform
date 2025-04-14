package site.shresthacyrus.neighborhoodhelpplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reviews")
@NoArgsConstructor
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private Integer rating; // e.g., 1 to 5

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // The user who wrote the review
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    // The user this review is about (target)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;

    // The job this review is related to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    // Photos associated with this review
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos;

    // === Lifecycle callback ===

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
