package in.edu.jspmjscoe.admissionportal.model.internship;

import in.edu.jspmjscoe.admissionportal.model.student.Student;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_profile", indexes = {
        @Index(name = "idx_student_profile_student", columnList = "student_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    // prnNumber is canonical on Student; avoid duplication here

    @Column(name = "branch")
    private String branch;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "current_semester")
    private Integer currentSemester;

    @Column(name = "current_cgpa")
    private Double currentCgpa;

    @Column(name = "aggregate_percentage")
    private Double aggregatePercentage;

    @Column(name = "dead_backlogs")
    private String deadBacklogs;

    @Column(name = "live_backlogs")
    private String liveBacklogs;

    @Column(name = "clear_backlogs_confidence")
    private String clearBacklogsConfidence;

    @Column(name = "tenth_percentage")
    private Double tenthPercentage;

    @Column(name = "tenth_board")
    private String tenthBoard;

    @Column(name = "twelfth_percentage")
    private Double twelfthPercentage;

    @Column(name = "twelfth_board")
    private String twelfthBoard;

    @Column(name = "diploma_percentage")
    private Double diplomaPercentage;

    @Column(name = "diploma_board")
    private String diplomaBoard;

    @Column(name = "career_interest")
    private String careerInterest;

    @Column(name = "higher_studies")
    private String higherStudies;

    @Column(name = "placement_interest")
    private String placementInterest;

    @Column(name = "relocation_interest")
    private String relocationInterest;

    @Column(name = "bond_acceptance")
    private String bondAcceptance;

    @Column(name = "certifications", length = 1000)
    private String certifications;

    @Column(name = "resume_path")
    private String resumePath;

    @Column(name = "pan_card")
    private String panCard;

    @Column(name = "aadhaar_card")
    private String aadhaarCard;

    @Column(name = "passport")
    private String passport;

    @Column(name = "local_city")
    private String localCity;

    @Column(name = "permanent_city")
    private String permanentCity;

    @Column(name = "permanent_state")
    private String permanentState;
}


