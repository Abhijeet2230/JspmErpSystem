package in.edu.jspmjscoe.admissionportal.model.subject;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import in.edu.jspmjscoe.admissionportal.model.teacher.TeacherSubject;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "subject_id")
    private Long subjectId;

    // Foreign key to Course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference // opposite side of Course.subjects
    private Course course;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "theory_credits")
    private Integer theoryCredits = 0;

    @Column(name = "practical_credits")
    private Integer practicalCredits = 0;

    @Column(name = "year_of_study", nullable = false)
    private Integer yearOfStudy;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "subject_type", length = 20)
    private String subjectType = "Theory"; // Theory, Lab, Project

    @Column(name = "subject_category", length = 20)
    private String subjectCategory = "Core"; // Core or Elective

    @Column(name = "total_units")
    private Integer totalUnits;

    // optional elective group
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "elective_group_id")
    @JsonBackReference // opposite side of ElectiveGroup.subjects
    private ElectiveGroup electiveGroup;

    // inside Subject.java
    @OneToMany(mappedBy = "subject",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TeacherSubject> teacherSubjects = new ArrayList<>();

    public void addTeacherSubject(TeacherSubject ts) {
        teacherSubjects.add(ts);
        ts.setSubject(this);
    }

    @Column(name = "status", length = 20)
    private String status = "Active";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
