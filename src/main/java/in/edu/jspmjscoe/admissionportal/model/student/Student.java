package in.edu.jspmjscoe.admissionportal.model.student;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import in.edu.jspmjscoe.admissionportal.model.security.User;
import in.edu.jspmjscoe.admissionportal.model.subject.Course;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "student_id")
    private Long studentId;   // independent PK

    // Unique foreign key to User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    @JsonManagedReference
    private User user;

    @Column(name = "application_id", unique = true)
    private String applicationId;

    @Column(name = "candidate_name")
    private String candidateName;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "roll_no")
    private Integer rollNo;

    @Column(name = "prn_number", unique = true, length = 20)
    private String prnNumber;   // PRN field

    @Column(name = "division")
    private String division;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "dob")
    private String dob;

    @Column(name = "religion")
    private String religion;

    @Column(name = "region")
    private String region;

    @Column(name = "mother_tongue")
    private String motherTongue;

    @Column(name = "annual_family_income")
    private String annualFamilyIncome;

    @Column(name = "candidature_type")
    private String candidatureType;

    @Column(name = "home_university")
    private String homeUniversity;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "category")
    private String category;

    @Column(name = "ph_type")
    private String phType;

    @Column(name = "defence_type")
    private String defenceType;

    @Column(name = "linguistic_minority")
    private String linguisticMinority;

    @Column(name = "religious_minority")
    private String religiousMinority;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Parent parent;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Address address;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private SSC ssc;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private HSC hsc;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EntranceExam> entranceExams = new ArrayList<>();

    // One student can have multiple admission records
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Admission> admissions = new ArrayList<>();

    // link to Course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference // opposite side of Course.students
    private Course course;

    public void addAdmission(Admission admission) {
        admissions.add(admission);
        admission.setStudent(this); // important
    }
}
