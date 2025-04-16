package site.shresthacyrus.neighborhoodhelpplatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.shresthacyrus.neighborhoodhelpplatform.common.RoleEnum;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String legalFirstName;
    @Column(nullable = false)
    private String legalLastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role; // SEEKER or HELPER

    private Boolean isMobileVerified = false;
    private Boolean isBackgroundVerified = false;
    private Boolean isPaymentVerified = false;

    @Column(nullable = false)
    private String passwordHash;

    // === Relationships ===

    // Jobs this user has posted (if seeker)
    @OneToMany(mappedBy = "seeker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Job> jobs;

    // Bids this user has placed (if helper)
    @OneToMany(mappedBy = "helper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bid> bids;

    // Reviews written by this user
    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> writtenReviews;

    // Reviews this user has received (NO cascade here)
    @OneToMany(mappedBy = "targetUser", fetch = FetchType.LAZY)
    private List<Review> receivedReviews;

    // Skill (many-to-many)
    @ManyToMany
    @JoinTable(
            name = "userSkills",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")

    )
    private List<Skill> skill;

    // Todo:
    // Photos associated with this user (e.g., profile, portfolio)
     @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
     private List<Photo> photos;

//    public User(String email, String phone, String legalFirstName, String legalLastName, RoleEnum role, String passwordHash) {
//        this.email = email;
//        this.phone = phone;
//        this.legalFirstName = legalFirstName;
//        this.legalLastName = legalLastName;
//        this.role = role;
//        this.passwordHash = passwordHash;
//
//        // Default verifications to false
//        this.isMobileVerified = false;
//        this.isBackgroundVerified = false;
//        this.isPaymentVerified = false;
//    }

    public String getLegalFullName() {
        return (legalFirstName + " " + legalLastName).trim();
    }


}
