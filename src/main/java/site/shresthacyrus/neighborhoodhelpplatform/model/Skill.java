package site.shresthacyrus.neighborhoodhelpplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.shresthacyrus.neighborhoodhelpplatform.common.JobStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "skills", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NoArgsConstructor
@Data
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "skill_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // === Relationships ===

    // Users who have this skill
    @ManyToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private List<User> users;   // User ->--------<- Skill

    // Jobs categorized under this skill
    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private List<Job> jobs; // Job ->--------- Skill

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // For seeding purposes
    public Skill(String name) {
        this.name = name;
    }
}
