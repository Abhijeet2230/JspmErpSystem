package in.edu.jspmjscoe.admissionportal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "teacher_id")
    private Long teacherId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonBackReference
    private Department department;

    // ========== 1. Personal Details ==========
    @Column(name = "prefix", length = 20)       // Mr., Dr., Prof.
    private String prefix;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "dob")
    private String dateOfBirth;   // use String or LocalDate

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "personal_email", length = 100)
    private String personalEmail;

    @Column(name = "aadhaar", length = 12, unique = true)
    private String aadhaarNumber;

    // ========== 2. Professional Details ==========
    @Column(name = "official_email", length = 100)
    private String officialEmail;   // JSPM email

    @Column(name = "designation", length = 50)
    private String designation;

    @Column(name = "employee_id", length = 50, unique = true)
    private String employeeId;

    @Column(name = "bcud_id", length = 50)
    private String bcudId;

    @Column(name = "vidwaan_id", length = 50)
    private String vidwaanId;

    @Column(name = "orchid_id", length = 50)
    private String orchidId;

    @Column(name = "google_scholar_id", length = 100)
    private String googleScholarId;

    // ========== 3. Academic Details ==========
    @Column(name = "highest_degree", length = 50)
    private String highestDegree;

    @Column(name = "phd_year")
    private Integer phdYear;

    @Column(name = "specialization", length = 100)
    private String specialization;

    @Column(name = "degree_university", length = 100)
    private String degreeUniversity;

    // ========== 4. Address (separate entity) ==========
    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private TeacherAddress address;

    // ========== 5. Work Experience ==========
    @Column(name = "previous_institutions", columnDefinition = "TEXT")
    private String previousInstitutions;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @Column(name = "subjects_taught", columnDefinition = "TEXT")
    private String subjectsTaught;

    // ========== 6. System fields ==========
    @Column(name = "status", length = 20)
    private String status = "Active";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TeacherSubject> teacherSubjects = new ArrayList<>();

    public void addTeacherSubject(TeacherSubject ts) {
        teacherSubjects.add(ts);
        ts.setTeacher(this);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
