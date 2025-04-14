package site.shresthacyrus.neighborhoodhelpplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
@NoArgsConstructor
@Data
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bid_id")
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;
    @Column(columnDefinition = "TEXT")
    private String message;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // === Relationships ===

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job; // Bid ->--------//- Job

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "helper_id", nullable = false)
    private User helper; // Bid ->--------//- User/Helper

    // === Lifecycle callback ===

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
