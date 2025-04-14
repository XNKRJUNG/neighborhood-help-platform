package site.shresthacyrus.neighborhoodhelpplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.shresthacyrus.neighborhoodhelpplatform.common.PhotoTypeEnum;

@Entity
@Table(name = "photos")
@NoArgsConstructor
@Data
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String url;

    private String caption;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhotoTypeEnum type;

    // === Relationships ===

    // Photo belongs to a user (e.g., profile or portfolio)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Photo attached to a job (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    // Photo attached to a review (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

}
