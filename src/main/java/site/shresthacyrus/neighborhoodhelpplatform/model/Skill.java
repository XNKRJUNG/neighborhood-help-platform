package site.shresthacyrus.neighborhoodhelpplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "skills")
@NoArgsConstructor
@Data
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "skill_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // === Relationships ===

    // Users who have this skill
    @ManyToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    private List<User> users;

    // Jobs categorized under this skill
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Job> jobs;
}
